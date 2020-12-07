package ux.async

import kotlinx.cinterop.*
import libgtk.gdk_threads_add_idle
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.internal.GC

/**Parameters used by workers that run the [doAsync] function.*/
private data class SimpleAsyncWorkerParameters<T, R>(
    /**The data passed into the the [producerFunction]. This is obtained from the `producerInput` function parameter of
     * the [doAsync] method, which is invoked on the thread that requested the work.*/
    val producerInput: T,
    /**A function to be executed inside the worker.*/
    val producerFunction: (T) -> R,
    /**A pointer to a [StableRef] containing the `callback` function parameter of the [doAsync] method.
     * This function is called on the GTK+ Main Thread (scheduled with [gdk_threads_add_idle]).*/
    val consumerWrapper: COpaquePointer?,
    /**A pointer to a [Byte] indicating the realtime status of the operation (see [Task.Status]).*/
    val statusFlagPointer: CPointer<ByteVar>,
)

/**Executes a heavyweight function on the worker and invokes [consumer] when finished. Rather than invoking this
 *  function directly, you should use [Worker.simpleTask].
 *
 * This operation is not safely cancellable, since it is run as a single blocking call. If you need to cancel the task
 *  in a safe way, consider using [doAsyncWithProgress].
 *
 * Before starting the worker, [producerInput] is called and its result is then passed *into* the worker, where it will
 *  be available as an implicit `it` parameter inside the [producer]'s sequence scope. This means that neither [producerInput]
 *  nor [producer] may contain/produce any externally referenced objects, since it may cause the operation to throw an
 *  exception or misbehave. The [producer] argument should be a frozen function reference.*/
public fun <T, R> Worker.doAsync(
    producerInput: () -> T,
    producer: (T) -> R,
    consumer: (R) -> Unit
): SimpleTask {
    val statusFlag: ByteVar = nativeHeap.alloc()

    execute(TransferMode.SAFE, {
        // Setup the worker: call producerInput, inline the producer and detach it, and wrap the consumer
        SimpleAsyncWorkerParameters(
            producerInput(),
            producer,
            StableRef.create(consumer).asCPointer(),
            statusFlag.ptr
        )
    }) { params ->
        // Mark the operation as "Running"
        params.statusFlagPointer.pointed.value = 1

        // Here we invoke the producer function and detach its result so it can be safely passed to the main thread
        // callback invoked by GDK.
        val graph = DetachedObjectGraph {
            val result = params.producerFunction(params.producerInput)

            CallbackSingleExecutionRequest(params.consumerWrapper, result).also {
                // Need to call the Garbage Collector manually, see the docs for DetachedObjectGraph.
                GC.collect()
            }
        }

        // Now we pass the graph on to C as a *void, this pointer will be unwrapped by `handleCallbackRequest`
        // (which is called by GDK), and the original consumer will be invoked using the result we supplied as an argument.
        gdk_threads_add_idle(handleCallbackRequest, graph.asCPointer())

        // Update the status flag to indicate that this work is finished
        params.statusFlagPointer.pointed.value = 2
    }

    return SimpleTask(this, statusFlag)
}

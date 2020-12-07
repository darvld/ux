package ux.async


import kotlinx.cinterop.*
import libgtk.g_main_context_find_source_by_id
import libgtk.g_source_set_callback
import libgtk.gdk_threads_add_idle
import ux.Void
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze
import kotlin.native.internal.GC


/**Parameters used by workers that run the [doAsyncWithProgress] function.*/
private data class AsyncWorkerParameters<T, R>(
    /**The data passed into the the [producerFunction]. This is obtained from the `producerInput` function parameter of
     * the [doAsyncWithProgress] method, which is invoked on the thread that requested the work.*/
    val inputData: T,
    /**A function to be executed inside the worker, a valid [SequenceScope] is provided, and the sequence's results are
     * collected and emitted accordingly.*/
    val producerFunction: AsyncWorkFun<T, R>,
    /**A pointer to a [StableRef] containing the `callback` function parameter of the [doAsyncWithProgress] method.
     * This function is called on the GTK+ Main Thread (scheduled with [gdk_threads_add_idle]).*/
    val consumerWrapper: COpaquePointer?,
    /**A pointer to a [Byte] indicating the realtime status of the operation (see [Task.Status]).*/
    val statusFlagPointer: CPointer<ByteVar>,
    /**Whether to cancel stale callbacks to increase performance.*/
    val optimizeCallbacks: Boolean = true
)

/**Executes a heavyweight function on the worker, asynchronously yielding values which will be consumed by [consumer]
 * on the GTK+ Main Thread.
 *
 * **Note:** Rather than invoking this function directly, you should use [Worker.deferredTask].
 *
 *
 * Before starting the worker, [producerInput] is called and its result is then passed *into* the worker, where it will
 * be available as an implicit `it` parameter inside the [producer]'s sequence scope. This means that neither [producerInput]
 * nor [producer] may contain/produce any externally referenced objects, since it may cause the operation to throw an
 * exception or misbehave. The [producer] argument should be a frozen function reference.
 *
 * The returned [Task] is cancellable, since this method checks for external cancellation in between sequence iterations.
 *
 * The [optimizeCallbacks] argument controls the cancellation of *stale* callbacks to improve performance.
 *
 * A callback is considered stale if does not run before the next sequence iteration. That is, if by the end of a
 * producer sequence iteration, the callback scheduled by the previous iteration has not been executed, then that
 * callback becomes stale.
 *
 * Note that, while this behaviour improves performance, it may be desirable to collect *all* the results of the
 * [producer] in some cases. If so, set this to false.*/
@ExperimentalUnsignedTypes
public fun <T, R> Worker.doAsyncWithProgress(
    producerInput: () -> T,
    producer: AsyncWorkFun<T, R>,
    /**Callback to be executed on the main thread to consume results yielded by [producer].
     *
     * Note that if [optimizeCallbacks] is true, then not *all* results might be consumed.*/
    consumer: (R) -> Unit,
    /***/
    optimizeCallbacks: Boolean = true
): Task {
    val statusFlag: ByteVar = nativeHeap.alloc()

    execute(TransferMode.SAFE, {
        AsyncWorkerParameters(
            producerInput(),
            producer,
            StableRef.create(consumer).asCPointer(),
            statusFlag.ptr,
            optimizeCallbacks
        )
    }) { data ->
        println("Entered Worker")
        //The source id of the last progress callback scheduled with gdk
        var lastId = 0u

        // The detached graph used as user-data on the last callback scheduled with gdk
        var lastGraph: Void? = null

        with(data) {
            // The result of invoking the `producer` function with the data supplied by `producerInput`
            val products: Sequence<Void?> = sequence {
                producerFunction(inputData)
            }.map {
                DetachedObjectGraph {
                    CallbackExecutionRequest(data.consumerWrapper, it.freeze()).also { GC.collect() }
                }.asCPointer()
            }

            // This operation is now officially running
            data.statusFlagPointer.pointed.value = 1

            // Consume all results produced by the sequence
            for (graph in products) {
                // Check for external cancellation
                if (data.statusFlagPointer.pointed.value == 3.toByte())
                    break

                // Now we check if the last scheduled callback has not executed and cancel it, this avoids unnecessary
                // callback executions (unless the user requested otherwise)
                if (data.optimizeCallbacks && lastId > 0u) {
                    // Try to locate the scheduled callback by its id.
                    g_main_context_find_source_by_id(GtkDefaultContext, lastId)?.let { source ->
                        // Change the handler so the previous graph is discarded instead of being unwrapped
                        g_source_set_callback(source, discardCallbackRequest, lastGraph, notify = null)
                    }
                }

                // Schedule a callback with the newly generated graph and update the last callback id and graph.
                lastId = gdk_threads_add_idle(handleCallbackRequest, graph)
                lastGraph = graph
            }
        }


        // A final callback request meant to dispose the StableRef containing the consumer, since that StableRef was
        // created on the main thread, it can only be accessed and disposed from that same thread.
        val graph = DetachedObjectGraph {
            CallbackCleanupRequest(data.consumerWrapper)
        }
        gdk_threads_add_idle(handleCallbackRequest, graph.asCPointer())

        // Update the status flag to indicate that this work is finished (if it was not cancelled)
        if (data.statusFlagPointer.pointed.value == 1.toByte())
            data.statusFlagPointer.pointed.value = 2
    }

    return Task(this, statusFlag)
}

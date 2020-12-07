@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ux.async

import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.freeze

/**Type safe builder used to create a [SimpleTask] instance.
 *
 * Use [withInput], [doWork] and [onFinish] to configure the task. Call [start] to begin the operation.*/
public class SimpleTaskBuilder<T, R>(public val worker: Worker = Worker.start()) {
    private var inputFun: () -> T? = { null }
    private var workFun: (T?) -> R? = { _ -> null }
    private var consumerFun: (R?) -> Unit = { }

    init {
        ensureNeverFrozen()
    }

    /**Use the provided [data] as input parameter for the worker function specified by [doWork], [data] must be frozen
     *  or otherwise immutable.
     *
     *  @see [withInput]*/
    public fun withInputData(data: T?): SimpleTaskBuilder<T, R> = apply {
        inputFun = { data }
    }

    /**Use the given [block] to produce the input data for the function specified by [doWork].
     *
     * The [block] will run on the current thread, and must not contain or produce any external references.
     *
     * @see [withInputData]*/
    public fun withInput(block: () -> T?): SimpleTaskBuilder<T, R> = apply {
        inputFun = block
    }

    /**Use the given [function] to produce a result on a worker thread, which will later be consumed by the
     * function specified by [onFinish].
     *
     * The [function] must not contain any external references, since it will be frozen (to avoid
     * IllegalTransferState exceptions).*/
    public fun doWork(function: (T?) -> R?): SimpleTaskBuilder<T, R> = apply {
        workFun = function.freeze()
    }

    /**Executes the given [block] when the work specified by [doWork] is finished, using its result as an argument.
     *
     * You may include external reference in this function since it will be executed on the main thread.*/
    public fun onFinish(block: (R?) -> Unit): SimpleTaskBuilder<T, R> = apply {
        consumerFun = block
    }

    /**Starts the configured operation.
     *
     * The returned [SimpleTask] instance can be used to track the progress of the operation and force its cancellation.*/
    public fun start(): SimpleTask {
        return worker.doAsync(inputFun, workFun, consumerFun)
    }
}

/**Creates a [SimpleTaskBuilder] with the receiver [Worker] as a constructor parameter.*/
public fun <T, R> Worker.simpleTask(): SimpleTaskBuilder<T, R> = SimpleTaskBuilder(this)

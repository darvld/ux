@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ux.async

import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze

public typealias AsyncWorkFun<T, R> = suspend SequenceScope<R>.(T) -> Unit

/**Type safe builder used to create a [Task] instance.
 *
 * Use [withInput], [doWork] and [onFinish] to configure the task.
 *
 * Call [start] to begin the operation.*/
public class TaskBuilder<T, R>(public val worker: Worker = Worker.start()) {
    private var inputFun: () -> T? = { null }
    private var workFun: AsyncWorkFun<T?, R?> = { }
    private var consumerFun: (R?) -> Unit = { }

    private var optimizeCallbacks: Boolean = true

    /**Use the provided [data] as input parameter for the worker function specified by [doWork], [data] must be frozen
     *  or otherwise immutable.
     *
     *  @see [withInput]*/
    public fun withInputData(data: T?): TaskBuilder<T, R> = apply {
        inputFun = { data }
    }

    /**Use the given [block] to produce the input data for the function specified by [doWork].
     *
     * The [block] will run on the current thread, and must not contain or produce any external references.
     *
     * @see [withInputData]*/
    public fun withInput(block: () -> T?): TaskBuilder<T, R> = apply {
        inputFun = block
    }

    /**Use the given [function] to produce a result on a worker thread, which will later be consumed by the
     * function specified by [onFinish].
     *
     * The [function] must not contain any external references, since it will be frozen (to avoid IllegalTransferState
     * exceptions).*/
    public fun doWork(function: AsyncWorkFun<T?, R?>): TaskBuilder<T, R> = apply {
        workFun = function
    }

    /**Executes the given [block] when the work specified by [doWork] is finished, using its result as an argument.
     *
     * You may include external reference in this function since it will be executed on the main thread.*/
    public fun onFinish(block: (R?) -> Unit): TaskBuilder<T, R> = apply {
        consumerFun = block
    }

    /**Adjusts whether stale callbacks should be removed to improve performance.
     *
     * Disable this if you need to receive all callbacks (i.e to collect all the results yielded by the
     * function specified by [doWork]).*/
    public fun optimizeCallbacks(enabled: Boolean): TaskBuilder<T, R> = apply {
        optimizeCallbacks = enabled
    }

    /**Start a [Task] with the previously supplied parameters.
     *
     * The returned object can be used to track the
     * operation's [status][Task.status] and cancel it.*/
    public fun start(): Task {
        return worker.doAsyncWithProgress(
            inputFun,
            workFun.freeze(),
            consumerFun,
            optimizeCallbacks
        )
    }
}

/**Creates a [TaskBuilder] with the receiver [Worker] as a constructor parameter.*/
public fun <T, R> Worker.deferredTask(): TaskBuilder<T, R> = TaskBuilder(this)

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ux.async

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.value
import kotlin.native.concurrent.Worker


/**Represents an operation that should be executed in the background, updating the ui as it is performed.
 *
 * The operation is cancellable since the work method produces a [Sequence] of values, which allows to check for
 * external cancellation in between iterations.*/
public class Task internal constructor(private val worker: Worker, private val statusFlag: ByteVar) {

    /**Represents the status of a [Task] or [SimpleTask] instance.*/
    public enum class Status {
        /**The work is not being executed yet.*/
        Queued,

        /**The work has entered the setup phase and is considered to be running input Producer method is about to
         *  be called).*/
        Running,

        /**The work returned normally and the operation is complete.*/
        Finished,

        /**The work was manually cancelled.*/
        Cancelled,

        /**An error occurred during the execution of this job.*/
        Error,
    }

    /**Whether to cancel the execution of scheduled callbacks corresponding to previous iterations. This increases the
     * performance of the operation, effectively yielding only the most recently emitted values.
     *
     * However, on some cases you might need to receive all callbacks, even if they are out of date (for example,
     * to collect the results contained in [AsyncDataFragment] instances). To do this, simply turn off this option.*/
    public var optimizeCallbacks: Boolean = true

    /**Options controlling the behaviour of the [cancel] method.*/
    public enum class CancelFlags {
        /**Simply request the cancellation of this operation, the actual cancellation may happen any time after this.*/
        None,

        /**Request the cancellation and stop the [worker] after it is complete. Note that this does not mean that the
         * cancellation happens right away.*/
        StopWorker,

        /**Same as [StopWorker], but awaits the [worker]'s result, blocking until it returns.
         *  See [Worker.requestTermination].*/
        StopWorkerAndJoin,

        /**Forces the worker's termination. This may lead to memory leaks.*/
        ForceStop,

        /**Forces the worker's termination and waits for it to complete. This may lead to memory leaks.*/
        ForceStopAndJoin
    }

    /**The current status of the operation. See [Task.Status] for details.*/
    public val status: Status
        get() = Status.values()[statusFlag.value.toInt()]

    /**Cancel this operation if it is running, use the specified [cancelFlags] to adjust the behaviour of this method.*/
    public fun cancel(cancelFlags: CancelFlags = CancelFlags.None) {
        if (status != Status.Running)
            return

        statusFlag.value = Status.Cancelled.ordinal.toByte()

        when (cancelFlags) {
            CancelFlags.StopWorker -> worker.requestTermination()
            CancelFlags.StopWorkerAndJoin -> worker.requestTermination().result
            CancelFlags.ForceStop -> worker.requestTermination(false)
            CancelFlags.ForceStopAndJoin -> worker.requestTermination(false).result
            CancelFlags.None -> {
            }
        }
    }

}

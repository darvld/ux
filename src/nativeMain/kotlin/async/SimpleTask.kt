@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ux.async

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.value
import kotlin.native.concurrent.Worker

/**Represents a simple operation that should be executed in the background, updating the ui when completed.
 *
 * The work is not cancellable in a safe way, since it acts as a single blocking method running inside a Worker.
 * If you need a safely cancellable operation, use [Task] instead.*/
public class SimpleTask(
    /**The [Worker] responsible for executing this operation.*/
    private val worker: Worker,
    /**A Byte representing the current status of the operation, used to communicate with the function
     * running inside the Worker.*/
    private val statusFlag: ByteVar
) {

    /**The current status of the operation. See [Task.Status] for details.*/
    public val status: Task.Status
        get() = Task.Status.values()[statusFlag.value.toInt()]

    /**Request the termination of the worker running this task. Note that this will usually lead to memory leaks since
     *  the cleanup phase of the operation will not be executed, so use this method as a last resort only. If you need
     *  to make an operation cancellation-aware, use [Task] instead, and split your task into shorter steps.*/
    public fun forceCancellation(join: Boolean = false) {
        if (status != Task.Status.Running)
            return

        if (join)
            worker.requestTermination(false).result
        else
            worker.requestTermination(false)
    }
}

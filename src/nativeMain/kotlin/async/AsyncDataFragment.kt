package ux.async

/**A fragment of data representing the current status of a [Task].
 *
 * Use this class if you need to expose some extra information to your ui callbacks.*/
public data class AsyncDataFragment<T>(
    /**A piece of data emitted during this iteration of the operation.*/
    public val data: T,
    /**The progress of the operation at the time of the fragment's emission (between 0 and 1).*/
    public val currentProgress: Double? = null,
    /**The current [String] message of the operation, this can be left as null if you don't need to expose a message.*/
    public val currentMessage: String? = null,
)

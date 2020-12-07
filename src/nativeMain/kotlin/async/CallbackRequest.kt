package ux.async

import kotlinx.cinterop.COpaquePointer

/**Helper class holding a stable reference to a user callback.
 *
 * Instances of this class are created in the [doAsync] and [doAsyncWithProgress] methods, and processed by
 * [handleCallbackRequest].*/
internal sealed class CallbackRequest(
    /**The user callback, wrapped in a StableRef and passed around as a C pointer.*/
    val callbackReference: COpaquePointer?
)

/**A request to execute a user callback using the given [callbackArgument].*/
internal class CallbackExecutionRequest(
    callbackReference: COpaquePointer?,
    /**The argument to be passed into the user callback when it is unwrapped.*/
    val callbackArgument: Any? = null
) : CallbackRequest(callbackReference)

/**A request to dispose the stable reference pointed by [callbackReference]. This is used by [doAsyncWithProgress] to
 * cleanup the consumer callback at the end of the operation, since disposing the handle inside the execution loop would
 * prevent the following iterations from calling the user-supplied function.*/
internal class CallbackCleanupRequest(callbackReference: COpaquePointer?) : CallbackRequest(callbackReference)

/**A request to execute a callback, using the given [callbackArgument] as a parameter and then disposing the stable reference pointed by
 * [callbackReference].*/
internal class CallbackSingleExecutionRequest(
    callbackReference: COpaquePointer?,
    /**The argument to be passed into the user callback when it is unwrapped.*/
    val callbackArgument: Any? = null
) : CallbackRequest(callbackReference)

package ux

import libgtk.GCallback
import libgtk.g_signal_connect_data
import libgtk.gpointer
import kotlinx.cinterop.*

/**Connects the specified [signal] and invokes the function referenced by [callbackWrapper].
 *
 * If your callback requires arguments you can specify a [handler] that accepts the amount you need,
 * the custom handler will need to reinterpret the [COpaquePointer?] arguments and correctly unwrap [callbackWrapper].*/
public fun Widget.connectSignal(
    signal: String,
    handler: CPointer<CFunction<() -> Unit>> = staticCallback,
    callbackWrapper: COpaquePointer? = null,
    flags: UInt = 0u
) {
    g_signal_connect_data(
        widgetPtr,
        signal,
        handler,
        callbackWrapper,
        staticCFunction { void: gpointer?, _ ->
            void?.asStableRef<Any>()?.dispose()
        },
        connect_flags = flags
    )
}

internal fun CPointer<*>.connectSignal(
    signal: String,
    handler: GCallback = staticCallback,
    callbackWrapper: COpaquePointer? = null,
    flags: UInt = 0u
) {
    g_signal_connect_data(
        this,
        signal,
        handler,
        callbackWrapper,
        staticCFunction { void: gpointer?, _ ->
            void?.asStableRef<Any>()?.dispose()
        },
        connect_flags = flags
    )
}

internal val staticCallback: GCallback = staticCFunction { _: gpointer?, data: gpointer? ->
    data?.asStableRef<() -> Unit>()?.get()?.invoke()

    Unit
}.reinterpret()

internal val staticCallback1: GCallback =
    staticCFunction { _: COpaquePointer?, arg: COpaquePointer?, data: COpaquePointer? ->
        data?.asStableRef<(COpaquePointer?) -> Unit>()?.get()?.invoke(arg)

        Unit
    }.reinterpret()

internal val staticCallback2: GCallback =
    staticCFunction { _: COpaquePointer?, arg1: COpaquePointer?, arg2: COpaquePointer?, data: COpaquePointer? ->
        data?.asStableRef<(COpaquePointer?, COpaquePointer?) -> Unit>()?.get()?.invoke(arg1, arg2)

        Unit
    }.reinterpret()

internal val staticCallback3: GCallback =
    staticCFunction { _: COpaquePointer?,
                      arg1: COpaquePointer?,
                      arg2: COpaquePointer?,
                      arg3: COpaquePointer?,
                      data: COpaquePointer? ->

        data?.asStableRef<(COpaquePointer?, COpaquePointer?, COpaquePointer?) -> Unit>()?.get()
            ?.invoke(arg1, arg2, arg3)

        Unit
    }.reinterpret()

package ux.async

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.staticCFunction
import libgtk.GMainContext
import libgtk.GSourceFunc
import libgtk.g_main_context_default
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.attach
import kotlin.native.internal.GC

/**Utility function used to cleanup [DetachedObjectGraph] instances used inside async operations.*/
internal val discardCallbackRequest: GSourceFunc = staticCFunction { data: COpaquePointer? ->
    DetachedObjectGraph<CallbackRequest>(data).attach()
    return@staticCFunction 0
}

/**Static C function responsible for unwrapping [CallbackRequest] instances emitted from async operations.*/
internal val handleCallbackRequest: GSourceFunc = staticCFunction { data: COpaquePointer? ->
    data ?: return@staticCFunction 0
    val graph = DetachedObjectGraph<CallbackRequest>(data)
    when (val request = graph.attach()) {
        is CallbackExecutionRequest -> {
            request.callbackReference!!
                .asStableRef<(Any?) -> Unit>()
                .get()
                .invoke(request.callbackArgument)
        }
        is CallbackSingleExecutionRequest -> {
            val stable = request.callbackReference!!
                .asStableRef<(Any?) -> Unit>()

            stable.get().invoke(request.callbackArgument)
            stable.dispose()
        }
        is CallbackCleanupRequest -> {
            request.callbackReference?.asStableRef<(Any?) -> Unit>()?.dispose()
        }
    }
    GC.collect()
    return@staticCFunction 0
}

@SharedImmutable
internal val GtkDefaultContext: CPointer<GMainContext>? by lazy {
    g_main_context_default()
}

@file:Suppress("unused")

package ux

import libgtk.*
import kotlinx.cinterop.*

/**A [Window] is a top-level widget which can contain other widgets. Windows normally have decorations that are under
 *  the control of the windowing system and allow the user to manipulate the window (resize it, move it, close it,...).*/
public open class Window internal constructor(pointer: CPointer<GtkWidget>) : Bin(pointer) {
    public constructor() : this(gtk_window_new(GtkWindowType.GTK_WINDOW_TOPLEVEL)!!)

    private val self: CPointer<GtkWindow> = pointer.reinterpret()

    /**The title of this window, displayed in its title bar.
     *
     * A good title should help a user distinguish this window from other windows they may have open and might
     * include the application name and current document filename, for example.*/
    public var title: String
        get() = gtk_window_get_title(self)?.toKString() ?: "untitled"
        set(value) {
            gtk_window_set_title(self, value)
        }

    /**The default size of the window.
     *
     *  A default size of -1 means to use the “natural” default size the [size request][Widget.requestedSize] of the window.
     *
     *  If the window’s “natural” size (its size request) is larger than the default,
     *  the default will be ignored.
     *
     *  The default size of a window only affects the first time a window is shown; if a window is hidden and re-shown,
     *  it will remember the size it had prior to hiding, rather than using the default size.
     *
     *  Windows can’t actually be 0x0 in size, they must be at least 1x1, passing 0 for width and height will result
     *  in a 1x1 default size.*/
    public var defaultSize: Vector
        get() {
            memScoped {
                val x = alloc<gintVar>()
                val y = alloc<gintVar>()
                gtk_window_get_default_size(self, x.ptr, y.ptr)

                return Vector(x.value, y.value)
            }
        }
        set(value) {
            gtk_window_set_default_size(self, value.x, value.y)
        }

    /**Whether the window should be decorated by the window manager.*/
    public var decorated: Boolean
        get() = Boolean from gtk_window_get_decorated(self)
        set(value) {

            gtk_window_set_decorated(self, value.gtkValue)
        }

    /**Whether the window is modal (other windows are not usable while this one is up).*/
    public var modal: Boolean
        get() = Boolean from gtk_window_get_modal(self)
        set(value) {
            gtk_window_set_modal(self, value.gtkValue)
        }

    /**Sets a custom title bar for this window.
     *
     * If you set a custom title bar, GTK+ will do its best to convince the window manager not to put its own title bar
     * on the window. Depending on the system, this function may not work for a window that is already visible, so you
     * should set the title bar before calling [show()][Widget.show].*/
    public fun setTitleBar(bar: Widget) {
        gtk_window_set_titlebar(self, bar.widgetPtr)
    }

    /**Requests the window to be closed, similar to what happens when a window manager close button is clicked.
     *
     * This function can be used with close buttons in custom title bars.*/
    public fun close() {
        gtk_window_close(self)
    }

    /**Resizes the window as if the user had done so, obeying geometry constraints. The default geometry constraint is
     *  that windows may not be smaller than their size request; to override this constraint, use [Widget.requestedSize]
     *  to set the window's request to a smaller value.*/
    public fun resize(width: Int, height: Int) {
        gtk_window_resize(self, width, height)
    }

}

@WidgetDsl
public inline fun window(
    title: String? = null,
    subtitle: String? = null,
    setup: Window.() -> Unit = {}
): Window {
    return Window().also { instance ->
        instance.headerBar(title, subtitle)

        instance.show()
        instance.setup()
    }
}

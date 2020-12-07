@file:Suppress("unused")

package ux

import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret



public open class ApplicationWindow internal constructor(pointer: CPointer<GtkWidget>) : Window(pointer) {
    public constructor(app: Application) : this(gtk_application_window_new(app.cpointer)!!)

    private val self: CPointer<GtkApplicationWindow> = pointer.reinterpret()

    public val id: UInt
        get() = gtk_application_window_get_id(self)

    public var showMenubar: Boolean
        get() = Boolean from gtk_application_window_get_show_menubar(self)
        set(value) {
            gtk_application_window_set_show_menubar(self, value.gtkValue)
        }

    public var helpOverlay: ShortcutsWindow?
        get() = gtk_application_window_get_help_overlay(self)?.let { ShortcutsWindow(it.reinterpret()) }
        set(value) {
            gtk_application_window_set_help_overlay(self, value?.self)
        }
}


@WidgetDsl
public fun Application.applicationWindow(
    title: String? = null,
    subtitle: String? = null,
    setup: ApplicationWindow.() -> Unit = {}
): ApplicationWindow {
    return ApplicationWindow(this).also { instance ->
        instance.headerBar(title, subtitle)

        instance.show()
        instance.setup()
    }
}

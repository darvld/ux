@file:Suppress("unused")

package ux


import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString

public open class ProgressBar internal constructor(
    pointer: CPointer<GtkWidget>
) : Widget(pointer) {
    public constructor() : this(gtk_progress_bar_new()!!)

    private val self: CPointer<GtkProgressBar> = pointer.reinterpret()

    public fun pulse() {
        gtk_progress_bar_pulse(self)
    }

    public var progress: Double
        get() = gtk_progress_bar_get_fraction(self)
        set(value) {
            gtk_progress_bar_set_fraction(self, value)
        }

    public var inverted: Boolean
        get() = Boolean from gtk_progress_bar_get_inverted(self)
        set(value) {
            gtk_progress_bar_set_inverted(self, value.gtkValue)
        }

    public var showText: Boolean
        get() = Boolean from gtk_progress_bar_get_show_text(self)
        set(value) {
            gtk_progress_bar_set_show_text(self, value.gtkValue)
        }

    public var text: String?
        get() = gtk_progress_bar_get_text(self)?.toKString()
        set(value) {
            gtk_progress_bar_set_text(self, value)
        }

    public var ellipsize: EllipsizeMode
        get() = EllipsizeMode from gtk_progress_bar_get_ellipsize(self)
        set(value) {
            gtk_progress_bar_set_ellipsize(self, value.gtkValue)
        }

    public var pulseStep: Double
        get() = gtk_progress_bar_get_pulse_step(self)
        set(value) {
            gtk_progress_bar_set_pulse_step(self, value)
        }
}


@WidgetDsl
public fun Container.progressBar(
    showText: Boolean = false,
    op: ProgressBar.() -> Unit = {}
) : ProgressBar {
    val b = ProgressBar()

    add(b)
    b.showText = showText

    b.show()
    b.op()
    return b
}

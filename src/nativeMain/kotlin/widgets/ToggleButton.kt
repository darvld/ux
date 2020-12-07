@file:Suppress("unused")

package ux




import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret

public open class ToggleButton internal constructor(
    pointer: CPointer<GtkWidget>
) : Button(pointer) {
    public constructor(label: String? = null) : this(gtk_toggle_button_new_with_label(label)!!)

    private val self: CPointer<GtkToggleButton> = pointer.reinterpret()

    public var active: Boolean
        get() = Boolean from gtk_toggle_button_get_active(self)
        set(value) {
            gtk_toggle_button_set_active(self, value.gtkValue)
        }

    public var inconsistent: Boolean
        get() = Boolean from gtk_toggle_button_get_inconsistent(self)
        set(value) {
            gtk_toggle_button_set_inconsistent(self, value.gtkValue)
        }

    public var drawIndicator: Boolean
        get() = Boolean from gtk_toggle_button_get_mode(self)
        set(value) {
            gtk_toggle_button_set_mode(self, value.gtkValue)
        }

    /**Emits the “toggled” signal on the GtkToggleButton. There is no good reason for an application ever to call this function.*/
    public fun toggled() {
        gtk_toggle_button_toggled(self)
    }

    public fun onToggled(callback: () -> Unit) {
        connectSignal(
            "toggled",
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }
}

@WidgetDsl
public fun Container.toggleButton(
    label: String? = null,
    op: ToggleButton.() -> Unit = {}
): ToggleButton {
    val b = ToggleButton(label)

    add(b)
    b.show()

    b.op()
    return b
}

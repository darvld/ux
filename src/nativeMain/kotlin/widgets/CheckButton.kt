@file:Suppress("unused")

package ux




import kotlinx.cinterop.CPointer
import libgtk.GtkWidget
import libgtk.gtk_check_button_new_with_label

public open class CheckButton internal constructor(
    pointer: WidgetPtr
) : ToggleButton(pointer) {
    public constructor(label: String? = null) : this(gtk_check_button_new_with_label(label)!!)

    // private val self: CPointer<GtkCheckButton> = pointer.reinterpret()
}


@WidgetDsl
public fun Container.checkButton(
    label: String? = null,
    op: CheckButton.() -> Unit = {}
): CheckButton {
    val b = CheckButton(label)

    add(b)
    b.show()

    b.op()
    return b
}

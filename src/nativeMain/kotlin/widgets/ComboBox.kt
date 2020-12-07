@file:Suppress("unused")

package ux


import libgtk.*
import kotlinx.cinterop.*

public open class ComboBox(
    pointer: CPointer<GtkWidget>
) : Bin(pointer) {
    public constructor() : this(gtk_combo_box_new()!!)

    private val self: CPointer<GtkComboBox> = pointer.reinterpret()

    public var wrapWidth: Int
        get() = gtk_combo_box_get_wrap_width(self)
        set(value) {
            gtk_combo_box_set_wrap_width(self, value)
        }

    public var activeItem: Int
        get() = gtk_combo_box_get_active(self)
        set(value) {
            gtk_combo_box_set_active(self, value)
        }


}

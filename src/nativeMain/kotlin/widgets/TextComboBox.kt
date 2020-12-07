@file:Suppress("unused")

package ux




import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString

public open class TextComboBox(
    pointer: CPointer<GtkWidget>
) : ComboBox(pointer) {
    public constructor(withEntry: Boolean = false) : this(
        if (withEntry) gtk_combo_box_text_new_with_entry()!! else gtk_combo_box_text_new()!!
    )

    private val self: CPointer<GtkComboBoxText> = pointer.reinterpret()

    public fun append(option: String, id: String? = null) {
        gtk_combo_box_text_append(self, id, option)
    }

    public fun prepend(option: String, id: String? = null) {
        gtk_combo_box_text_prepend(self, id, option)
    }

    public fun insert(position: Int, option: String, id: String? = null) {
        gtk_combo_box_text_insert(self, position, id, option)
    }

    public fun remove(position: Int) {
        gtk_combo_box_text_remove(self, position)
    }

    public fun removeAll() {
        gtk_combo_box_text_remove_all(self)
    }

    public val activeText: String?
        get() = gtk_combo_box_text_get_active_text(self)?.toKString()
}

@WidgetDsl
public fun Container.textComboBox(
    vararg options: String,
    withEntry: Boolean = false,
    setup: TextComboBox.() -> Unit
): TextComboBox {
    val view = TextComboBox(withEntry)

    add(view)
    view.show()

    for (opt in options) {
        view.append(opt)
    }

    view.setup()
    return view
}

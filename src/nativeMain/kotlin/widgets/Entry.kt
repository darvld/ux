@file:Suppress("unused")

package ux




import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString


public class Entry(
    pointer: CPointer<GtkWidget>
) : Widget(pointer) {
    public constructor() : this(gtk_entry_new()!!)
    public constructor(buffer: EntryBuffer) : this(gtk_entry_new_with_buffer(buffer.self)!!)

    private val self: CPointer<GtkEntry> = pointer.reinterpret()

    public var buffer: EntryBuffer
        get() = EntryBuffer(gtk_entry_get_buffer(self)!!)
        set(value) {
            gtk_entry_set_buffer(self, value.self)
        }

    public var text: String
        get() = gtk_entry_get_text(self)?.toKString() ?: ""
        set(value) {
            gtk_entry_set_text(self, value)
        }

    public val textLength: Int
        get() = gtk_entry_get_text_length(self).convert()

    public var textVisible: Boolean
        get() = Boolean from gtk_entry_get_visibility(self)
        set(value) {
            gtk_entry_set_visibility(self, value.gtkValue)
        }

    public var invisibleChar: Char
        get() = gtk_entry_get_invisible_char(self).toInt().toChar()
        set(value) {
            gtk_entry_set_invisible_char(self, value.toInt().convert())
        }

    public var maxLength: Int
        get() = gtk_entry_get_max_length(self)
        set(value) {
            gtk_entry_set_max_length(self, value)
        }

    public var hasFrame: Boolean
        get() = Boolean from gtk_entry_get_has_frame(self)
        set(value) {
            gtk_entry_set_has_frame(self, value.gtkValue)
        }

    public var widthChars: Int
        get() = gtk_entry_get_width_chars(self)
        set(value) {
            gtk_entry_set_width_chars(self, value)
        }

    public var maxWidthChars: Int
        get() = gtk_entry_get_max_width_chars(self)
        set(value) {
            gtk_entry_set_max_width_chars(self, value)
        }

    public var alignment: Float
        get() = gtk_entry_get_alignment(self)
        set(value) {
            gtk_entry_set_alignment(self, value)
        }

    public var placeholderText: String?
        get() = gtk_entry_get_placeholder_text(self)?.toKString()
        set(value) {
            gtk_entry_set_placeholder_text(self, value)
        }

    public var overwriteMode: Boolean
        get() = Boolean from gtk_entry_get_overwrite_mode(self)
        set(value) {
            gtk_entry_set_overwrite_mode(self, value.gtkValue)
        }

    // TODO: 11/28/2020 Completion stuff
}

@WidgetDsl
public fun Container.textField(
    buffer: EntryBuffer? = null,
    op: Entry.() -> Unit = {}
): Entry {
    val view = buffer?.let { Entry(it) } ?: Entry()

    add(view)
    view.show()

    view.op()
    return view
}

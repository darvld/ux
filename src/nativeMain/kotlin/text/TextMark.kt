@file:Suppress("unused")

package ux




import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

public class TextMark internal constructor(internal val self: CPointer<GtkTextMark>) {
    public constructor(name: String? = null, leftGravity: Boolean = false) : this(
        gtk_text_mark_new(
            name,
            leftGravity.gtkValue
        )!!
    )

    public val name: String?
        get() = gtk_text_mark_get_name(self)?.toKString()

    public var visible: Boolean
        get() = Boolean from gtk_text_mark_get_visible(self)
        set(value) {
            gtk_text_mark_set_visible(self, value.gtkValue)
        }

    public val deleted: Boolean
        get() = Boolean from gtk_text_mark_get_deleted(self)


    public val buffer: TextBuffer?
        get() = gtk_text_mark_get_buffer(self)?.let { TextBuffer(it) }

    public val leftGravity: Boolean
        get() = Boolean from gtk_text_mark_get_left_gravity(self)
}

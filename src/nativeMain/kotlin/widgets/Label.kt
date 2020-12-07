@file:Suppress("unused")

package ux

import libgtk.*

import kotlinx.cinterop.*

public open class Label internal constructor(
    pointer: CPointer<GtkWidget>
) : Widget(pointer) {
    public constructor(text: String?) : this(gtk_label_new(text)!!)

    private val self: CPointer<GtkLabel> = pointer.reinterpret()

    public var text: String
        get() = gtk_label_get_text(self)?.toKString() ?: ""
        set(value) {
            gtk_label_set_text(self, value)
        }

    public fun setUnderlinePattern(pattern: String?) {
        gtk_label_set_pattern(self, pattern)
    }

    public var justify: Justify
        get() = Justify from gtk_label_get_justify(self)
        set(value) {
            gtk_label_set_justify(self, value.gtkValue)
        }

    public var xAlign: Float
        get() = gtk_label_get_xalign(self)
        set(value) {
            gtk_label_set_xalign(self, value)
        }

    public var yAlign: Float
        get() = gtk_label_get_yalign(self)
        set(value) {
            gtk_label_set_yalign(self, value)
        }

    public var ellipsizeMode: EllipsizeMode
        get() = EllipsizeMode from gtk_label_get_ellipsize(self)
        set(value) {
            gtk_label_set_ellipsize(self, value.gtkValue)
        }

    public var widthChars: Int
        get() = gtk_label_get_width_chars(self)
        set(value) {
            gtk_label_set_width_chars(self, value)
        }

    public var maxWidthChars: Int
        get() = gtk_label_get_max_width_chars(self)
        set(value) {
            gtk_label_set_max_width_chars(self, value)
        }

    public var lineWrap: Boolean
        get() = Boolean from gtk_label_get_line_wrap(self)
        set(value) {
            gtk_label_set_line_wrap(self, value.gtkValue)
        }

    public var lineWrapMode: WrapMode
        get() = WrapMode from gtk_label_get_line_wrap_mode(self)
        set(value) {
            gtk_label_set_line_wrap_mode(self, value.gtkValue)
        }

    public var lines: Int
        get() = gtk_label_get_lines(self)
        set(value) {
            gtk_label_set_lines(self, value)
        }

    public var wrapMode: WrapMode by obj.gProperty("wrap-mode") { WrapMode.values()[it.asEnumOrdinal] }

    public val layoutOffsets: Vector
        get() = memScoped {
            val x = alloc<gintVar>()
            val y = alloc<gintVar>()

            gtk_label_get_layout_offsets(self, x.ptr, y.ptr)

            Vector(x.value, y.value)
        }

    public var selectable: Boolean
        get() = Boolean from gtk_label_get_selectable(self)
        set(value) {
            gtk_label_set_selectable(self, value.gtkValue)
        }

    public var selectionBounds: IntRange
        get() = memScoped {
            val start = alloc<gintVar>()
            val end = alloc<gintVar>()

            gtk_label_get_selection_bounds(self, start.ptr, end.ptr)

            start.value..end.value
        }
        set(value) {
            gtk_label_select_region(self, value.first, value.last)
        }

    public var useMarkup: Boolean
        get() = Boolean from gtk_label_get_use_markup(self)
        set(value) {
            gtk_label_set_use_markup(self, value.gtkValue)
        }

    public var useUnderline: Boolean
        get() = Boolean from gtk_label_get_use_underline(self)
        set(value) {
            gtk_label_set_use_underline(self, value.gtkValue)
        }

    public var singleLine: Boolean
        get() = Boolean from gtk_label_get_single_line_mode(self)
        set(value) {
            gtk_label_set_single_line_mode(self, value.gtkValue)
        }

    public var angle: Double
        get() = gtk_label_get_angle(self)
        set(value) {
            gtk_label_set_angle(self, value)
        }

    public val currentUri: String?
        get() = gtk_label_get_current_uri(self)?.toKString()

    public var label: String
        get() = gtk_label_get_label(self)?.toKString() ?: ""
        set(value) {
            gtk_label_set_label(self, value)
        }

    public var trackVisitedLinks: Boolean
        get() = Boolean from gtk_label_get_track_visited_links(self)
        set(value) {
            gtk_label_set_track_visited_links(self, value.gtkValue)
        }
}

@WidgetDsl
public fun Container.label(
    text: String? = null,
    op: Label.() -> Unit = {}
): Label {
    val b = Label(text)

    add(b)
    b.show()

    b.op()
    return b
}

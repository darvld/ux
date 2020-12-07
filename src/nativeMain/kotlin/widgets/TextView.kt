@file:Suppress("unused")

package ux


import kotlinx.cinterop.*
import libgtk.*


public open class TextView internal constructor(
    pointer: CPointer<GtkWidget>,
    public val buffer: TextBuffer = TextBuffer(gtk_text_view_get_buffer(pointer.reinterpret())!!)
) : Container(pointer) {
    public constructor(buffer: TextBuffer) : this(gtk_text_view_new_with_buffer(buffer.self)!!, buffer)
    public constructor() : this(gtk_text_view_new()!!)

    private val self: CPointer<GtkTextView> = pointer.reinterpret()

    public inline var text: String
        get() = buffer.text
        set(value) {
            buffer.text = value
        }


    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////

    public val visibleRect: Rectangle
        get() = memScoped {
            val rect = alloc<GdkRectangle>()
            gtk_text_view_get_visible_rect(self, rect.ptr)

            return@memScoped Rectangle from rect
        }

    public var editable: Boolean
        get() = Boolean from gtk_text_view_get_editable(self)
        set(value) {
            gtk_text_view_set_editable(self, value.gtkValue)
        }

    public var overwrite: Boolean
        get() = Boolean from gtk_text_view_get_overwrite(self)
        set(value) {
            gtk_text_view_set_overwrite(self, value.gtkValue)
        }

    public var wrapMode: GtkWrapMode
        get() = gtk_text_view_get_wrap_mode(self)
        set(value) {
            gtk_text_view_set_wrap_mode(self, value)
        }

    public var cursorVisible: Boolean
        get() = Boolean from gtk_text_view_get_cursor_visible(self)
        set(value) {
            gtk_text_view_set_cursor_visible(self, value.gtkValue)
        }

    public var pixelsAboveLines: Int
        get() = gtk_text_view_get_pixels_above_lines(self)
        set(value) {
            gtk_text_view_set_pixels_above_lines(self, value)
        }

    public var pixelsBelowLines: Int
        get() = gtk_text_view_get_pixels_below_lines(self)
        set(value) {
            gtk_text_view_set_pixels_below_lines(self, value)
        }

    public var pixelsInsideWrap: Int
        get() = gtk_text_view_get_pixels_inside_wrap(self)
        set(value) {
            gtk_text_view_set_pixels_inside_wrap(self, value)
        }

    public var justification: Justify
        get() = Justify from gtk_text_view_get_justification(self)
        set(value) {
            gtk_text_view_set_justification(self, value.gtkValue)
        }

    public var leftMargin: Int
        get() = gtk_text_view_get_left_margin(self)
        set(value) {
            gtk_text_view_set_left_margin(self, value)
        }

    public var rightMargin: Int
        get() = gtk_text_view_get_right_margin(self)
        set(value) {
            gtk_text_view_set_right_margin(self, value)
        }

    public var topMargin: Int
        get() = gtk_text_view_get_top_margin(self)
        set(value) {
            gtk_text_view_set_top_margin(self, value)
        }

    public var bottomMargin: Int
        get() = gtk_text_view_get_bottom_margin(self)
        set(value) {
            gtk_text_view_set_bottom_margin(self, value)
        }

    public var indent: Int
        get() = gtk_text_view_get_indent(self)
        set(value) {
            gtk_text_view_set_indent(self, value)
        }

    public var acceptsTab: Boolean
        get() = Boolean from gtk_text_view_get_accepts_tab(self)
        set(value) {
            gtk_text_view_set_accepts_tab(self, value.gtkValue)
        }

    public var monospace: Boolean
        get() = Boolean from gtk_text_view_get_monospace(self)
        set(value) {
            gtk_text_view_set_monospace(self, value.gtkValue)
        }
    
    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    public fun scrollToMark(
        mark: TextMark,
        withinMargin: Double,
        useAlign: Boolean,
        xAlign: Double,
        yAlign: Double
    ) {
        gtk_text_view_scroll_to_mark(self, mark.self, withinMargin, useAlign.gtkValue, xAlign, yAlign)
    }

    public fun scrollToIter(
        iter: TextIter,
        withinMargin: Double,
        useAlign: Boolean,
        xAlign: Double,
        yAlign: Double
    ) {
        gtk_text_view_scroll_to_iter(self, iter.self, withinMargin, useAlign.gtkValue, xAlign, yAlign)
    }

    public fun moveMarkOnScreen(mark: TextMark) {
        gtk_text_view_move_mark_onscreen(self, mark.self)
    }

    public fun placeCursorOnScreen() {
        gtk_text_view_place_cursor_onscreen(self)
    }

    public fun getIterLocation(iter: TextIter): Rectangle = memScoped {
        val rect = alloc<GdkRectangle>()
        gtk_text_view_get_iter_location(self, iter.self, rect.ptr)

        return Rectangle from rect
    }

    public fun getCursorLocations(iter: TextIter? = null): Pair<Rectangle, Rectangle> = memScoped {
        val strong = alloc<GdkRectangle>()
        val weak = alloc<GdkRectangle>()

        gtk_text_view_get_cursor_locations(self, iter?.self, strong.ptr, weak.ptr)
        return Rectangle.from(strong) to Rectangle.from(weak)
    }

    public fun getLineAtY(y: Int): Pair<TextIter, Int> = memScoped {
        val iter = nativeHeap.alloc<GtkTextIter>().ptr
        val top = alloc<IntVar>()
        gtk_text_view_get_line_at_y(self, iter, y, top.ptr)

        return TextIter(iter) to top.value
    }

    public fun getLineYRange(iter: TextIter): Pair<Int, Int> = memScoped {
        val y = alloc<IntVar>()
        val height = alloc<IntVar>()

        gtk_text_view_get_line_yrange(self, iter.self, y.ptr, height.ptr)
        return y.value to height.value
    }

    public fun getIterAtLocation(x: Int, y: Int): TextIter = memScoped {
        val iter = nativeHeap.alloc<GtkTextIter>().ptr
        gtk_text_view_get_iter_at_location(self, iter, x, y)
        return TextIter(iter)
    }

    public fun getIterAtPosition(x: Int, y: Int): Pair<TextIter, Int> = memScoped {
        val iter = nativeHeap.alloc<GtkTextIter>().ptr
        val trailing = alloc<IntVar>()

        gtk_text_view_get_iter_at_position(self, iter, trailing.ptr, x, y)

        return TextIter(iter) to trailing.value
    }

    public fun bufferToWindowCoords(windowType: TextWindowType, x: Int, y: Int): Vector = memScoped {
        val wx = alloc<IntVar>()
        val wy = alloc<IntVar>()
        gtk_text_view_buffer_to_window_coords(self, windowType.gtkValue, x, y, wy.ptr, wy.ptr)

        return wx.value by wy.value
    }

    public fun windowToBufferCoords(windowType: TextWindowType, x: Int, y: Int): Vector = memScoped {
        val bx = alloc<IntVar>()
        val by = alloc<IntVar>()
        gtk_text_view_buffer_to_window_coords(self, windowType.gtkValue, x, y, by.ptr, by.ptr)

        return bx.value by by.value
    }

    public fun getBorderWindowSize(windowType: TextWindowType): Int {
        return gtk_text_view_get_border_window_size(self, windowType.gtkValue)
    }

    public fun setBorderWindowSize(windowType: TextWindowType, size: Int) {
        gtk_text_view_set_border_window_size(self, windowType.gtkValue, size)
    }

    public fun forwardDisplayLine(iter: TextIter): Boolean {
        return gtk_text_view_forward_display_line(self, iter.self) == 1
    }

    public fun forwardDisplayLineEnd(iter: TextIter): Boolean {
        return gtk_text_view_forward_display_line_end(self, iter.self) == 1
    }

    public fun backwardDisplayLine(iter: TextIter): Boolean {
        return gtk_text_view_backward_display_line(self, iter.self) == 1
    }

    public fun backwardDisplayLineStart(iter: TextIter): Boolean {
        return gtk_text_view_backward_display_line_start(self, iter.self) == 1
    }

    public fun startsDisplayLine(iter: TextIter): Boolean {
        return gtk_text_view_starts_display_line(self, iter.self) == 1
    }

    public fun moveVisually(iter: TextIter, count: Int): Boolean {
        return gtk_text_view_move_visually(self, iter.self, count) == 1
    }

    public fun addChildAtAnchor(child: Widget, anchor: TextChildAnchor) {
        gtk_text_view_add_child_at_anchor(self, child.widgetPtr, anchor.self)
    }

    public fun addChildInWindow(child: Widget, windowType: TextWindowType, x: Int, y: Int) {
        gtk_text_view_add_child_in_window(self, child.widgetPtr, windowType.gtkValue, x, y)
    }

    public fun moveChild(child: Widget, x: Int, y: Int) {
        gtk_text_view_move_child(self, child.widgetPtr, x, y)
    }

    public fun resetCursorBlink() {
        gtk_text_view_reset_cursor_blink(self)
    }

    // Synthetic
    public fun scrollToBottom() {
        gtk_text_view_scroll_mark_onscreen(self, buffer.endMark.self)
    }

    public inline fun appendText(text: String) {
        buffer.insert(text, buffer.end)
    }

    public inline fun writeln(line: String) {
        appendText("$line\n")
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////

    // Synthetic
    public inline fun onTextChanged(noinline callback: () -> Unit) {
        buffer.onChanged(callback)
    }
}

@WidgetDsl
public inline fun Container.textView(
    buffer: TextBuffer? = null,
    op: TextView.() -> Unit = {}
): TextView {
    val b = buffer?.let { TextView(it) } ?: TextView()

    add(b)
    b.show()

    b.op()
    return b
}

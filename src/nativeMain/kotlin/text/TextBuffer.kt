@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ux



import kotlinx.cinterop.*
import libgtk.*

public class TextBuffer internal constructor(internal val self: CPointer<GtkTextBuffer>) {
    public constructor() : this(gtk_text_buffer_new(null)!!)

    internal val endMark: TextMark by lazy {
        createMark("_END_", bounds.second)
    }

    private val arena = Arena()

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////

    public val lineCount: Int
        get() = gtk_text_buffer_get_line_count(self)

    public val charCount: Int
        get() = gtk_text_buffer_get_char_count(self)

    public val tagTable: TextTagTable
        get() = TextTagTable(gtk_text_buffer_get_tag_table(self)!!)

    public val hasSelection: Boolean
        get() = Boolean from gtk_text_buffer_get_has_selection(self)

    public val bounds: Pair<TextIter, TextIter>
        get() {
            gtk_text_buffer_get_bounds(self, start.self, end.self)

            return startIter to endIter
        }

    public var text: String
        get() {
            val str = gtk_text_buffer_get_text(self, start.self, end.self, 0)
            return (str?.toKString() ?: "").also { g_free(str) }
        }
        set(value) {
            gtk_text_buffer_set_text(self, value, value.length)
        }

    public var modified: Boolean
        get() = Boolean from gtk_text_buffer_get_modified(self)
        set(value) {
            gtk_text_buffer_set_modified(self, value.gtkValue)
        }

    private val startIter: TextIter = TextIter(arena.alloc<GtkTextIter>().ptr)
    public val start: TextIter
        get() {
            gtk_text_buffer_get_start_iter(self, startIter.self)
            return startIter
        }

    private val endIter: TextIter = TextIter(arena.alloc<GtkTextIter>().ptr)
    public val end: TextIter
        get() {
            gtk_text_buffer_get_end_iter(self, endIter.self)
            return startIter
        }

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    //                     Content-related operations                        //
    ///////////////////////////////////////////////////////////////////////////

    public fun getText(start: TextIter, end: TextIter, includeHidden: Boolean) {
        gtk_text_buffer_get_text(self, start.self, end.self, includeHidden.gtkValue)
    }

    public fun getSlice(start: TextIter, end: TextIter, includeHidden: Boolean): String {
        return gtk_text_buffer_get_slice(
            self,
            start.self,
            end.self,
            includeHidden.gtkValue
        )!!.toKString()
    }

    public fun insert(text: String, position: TextIter) {
        gtk_text_buffer_insert(self, position.self, text, text.length)
    }

    public fun insertInteractive(text: String, position: TextIter, defaultEditable: Boolean) {
        gtk_text_buffer_insert_interactive(self, position.self, text, text.length, defaultEditable.gtkValue)
    }

    public fun insertAtCursor(text: String) {
        gtk_text_buffer_insert_at_cursor(self, text, text.length)
    }

    public fun insertAtCursorInteractive(text: String, defaultEditable: Boolean) {
        gtk_text_buffer_insert_interactive_at_cursor(self, text, text.length, defaultEditable.gtkValue)
    }

    public fun insertRange(position: TextIter, start: TextIter, end: TextIter) {
        gtk_text_buffer_insert_range(self, position.self, start.self, end.self)
    }

    public fun insertRangeInteractive(
        position: TextIter,
        start: TextIter,
        end: TextIter,
        defaultEditable: Boolean
    ) {
        gtk_text_buffer_insert_range_interactive(
            self,
            position.self,
            start.self,
            end.self,
            defaultEditable.gtkValue
        )
    }

    public fun delete(start: TextIter, end: TextIter) {
        gtk_text_buffer_delete(self, start.self, end.self)
    }

    public fun deleteInteractive(start: TextIter, end: TextIter, defaultEditable: Boolean) {
        gtk_text_buffer_delete_interactive(self, start.self, end.self, defaultEditable.gtkValue)
    }

    public fun deleteSelection(interactive: Boolean = false, defaultEditable: Boolean = true): Boolean {
        return Boolean from gtk_text_buffer_delete_selection(
            self,
            interactive.gtkValue,
            defaultEditable.gtkValue
        )
    }

    public fun backspace(position: TextIter, interactive: Boolean, defaultEditable: Boolean) {
        gtk_text_buffer_backspace(self, position.self, interactive.gtkValue, defaultEditable.gtkValue)
    }

    public fun insertMarkup(position: TextIter, markup: String) {
        gtk_text_buffer_insert_markup(self, position.self, markup, markup.length)
    }

    /*public fun insertWithTags(position: TextIterator, text: String, vararg tags: TextTag) {
        gtk_text_buffer_insert_with_tags(
            self,
            position.self,
            text,
            text.length,
            tags.first().self,
            tags.drop(1).map { it.self }.toTypedArray(),
            null
        )
    }*/

    /*public fun insertWithTags(position: TextIterator, text: String, vararg tags: String) {
        gtk_text_buffer_insert_with_tags_by_name(
            self,
            position.self,
            text,
            text.length,
            tags.first(),
            *tags.drop(1).toTypedArray(),
            null
        )
    }*/

    ///////////////////////////////////////////////////////////////////////////
    //                                Tags                                   //
    ///////////////////////////////////////////////////////////////////////////

    public fun applyTag(tag: TextTag, start: TextIter, end: TextIter) {
        gtk_text_buffer_apply_tag(self, tag.self, start.self, end.self)
    }

    public fun removeAllTags(start: TextIter, end: TextIter) {
        gtk_text_buffer_remove_all_tags(self, start.self, end.self)
    }

    public fun createTag(name: String? = null, setup: TextTag.() -> Unit = {}): TextTag {
        val ptr = gtk_text_buffer_create_tag(self, name, null, null)!!
        return TextTag(ptr).apply(setup)
    }

    public fun removeTag(tag: TextTag, start: TextIter, end: TextIter) {
        gtk_text_buffer_remove_tag(self, tag.self, start.self, end.self)
    }

    ///////////////////////////////////////////////////////////////////////////
    //                                 Marks                                 //
    ///////////////////////////////////////////////////////////////////////////

    public fun addMark(mark: TextMark, position: TextIter) {
        gtk_text_buffer_add_mark(self, mark.self, position.self)
    }

    public fun createMark(name: String?, position: TextIter, leftGravity: Boolean = false): TextMark {
        val pointer = gtk_text_buffer_create_mark(self, name, position.self, leftGravity.gtkValue)
        return TextMark(pointer!!)
    }

    public fun getMark(name: String): TextMark? {
        return gtk_text_buffer_get_mark(self, name)?.let { TextMark(it) }
    }

    public fun getCursor(): TextMark {
        return TextMark(gtk_text_buffer_get_insert(self)!!)
    }

    public fun getSelectionBound(): TextMark {
        return TextMark(gtk_text_buffer_get_selection_bound(self)!!)
    }

    public fun moveMark(mark: TextMark, position: TextIter) {
        gtk_text_buffer_move_mark(self, mark.self, position.self)
    }

    public fun moveMark(name: String, position: TextIter) {
        gtk_text_buffer_move_mark_by_name(self, name, position.self)
    }

    public fun deleteMark(mark: TextMark) {
        gtk_text_buffer_delete_mark(self, mark.self)
    }

    public fun deleteMark(name: String) {
        gtk_text_buffer_delete_mark_by_name(self, name)
    }

    public fun insertImage(position: TextIter, image: Pixbuf) {
        gtk_text_buffer_insert_pixbuf(self, position.self, image.cpointer)
    }

    public fun createChildAnchor(position: TextIter): TextChildAnchor {
        return TextChildAnchor(gtk_text_buffer_create_child_anchor(self, position.self)!!)
    }

    // Synthetic
    public fun getIterator(offset: Int): TextIter {
        val pointer = arena.alloc<GtkTextIter>().ptr
        gtk_text_buffer_get_iter_at_offset(self, pointer, offset)

        return TextIter(pointer)
    }

    ///////////////////////////////////////////////////////////////////////////
    //                                 Misc                                  //
    ///////////////////////////////////////////////////////////////////////////

    public fun placeCursor(position: TextIter) {
        gtk_text_buffer_place_cursor(self, position.self)
    }

    public fun selectRange(start: TextIter, end: TextIter) {
        gtk_text_buffer_select_range(self, start.self, end.self)
    }

    public fun getIteratorAtLine(lineNumber: Int): TextIter = memScoped {
        val ptr = alloc<GtkTextIter>().ptr
        gtk_text_buffer_get_iter_at_line(self, ptr, lineNumber)
        TextIter(ptr)
    }

    public fun getIteratorAtLineOffset(lineNumber: Int, offset: Int): TextIter = memScoped {
        val ptr = alloc<GtkTextIter>().ptr
        gtk_text_buffer_get_iter_at_line_offset(self, ptr, lineNumber, offset)
        TextIter(ptr)
    }

    public fun getIteratorAtLineIndex(lineNumber: Int, byteIndex: Int): TextIter = memScoped {
        val ptr = alloc<GtkTextIter>().ptr
        gtk_text_buffer_get_iter_at_line_index(self, ptr, lineNumber, byteIndex)
        TextIter(ptr)
    }

    public fun getIteratorAtMark(mark: TextMark): TextIter = memScoped {
        val ptr = alloc<GtkTextIter>().ptr
        gtk_text_buffer_get_iter_at_mark(self, ptr, mark.self)
        TextIter(ptr)
    }

    public fun getIteratorAtOffset(offset: Int): TextIter = memScoped {
        val ptr = alloc<GtkTextIter>().ptr
        gtk_text_buffer_get_iter_at_offset(self, ptr, offset)
        TextIter(ptr)
    }

    public fun getIteratorAtChildAnchor(anchor: TextChildAnchor): TextIter = memScoped {
        val ptr = alloc<GtkTextIter>().ptr
        gtk_text_buffer_get_iter_at_child_anchor(self, ptr, anchor.self)
        TextIter(ptr)
    }

    public fun pasteClipboard(
        clipboard: Clipboard,
        defaultEditable: Boolean = true,
        overrideLocation: TextIter? = null
    ) {
        gtk_text_buffer_paste_clipboard(
            self,
            clipboard.cpointer,
            overrideLocation?.self,
            defaultEditable.gtkValue
        )
    }

    public fun copyClipboard(clipboard: Clipboard) {
        gtk_text_buffer_copy_clipboard(self, clipboard.cpointer)
    }

    public fun cutClipboard(clipboard: Clipboard, defaultEditable: Boolean = true) {
        gtk_text_buffer_cut_clipboard(self, clipboard.cpointer, defaultEditable.gtkValue)
    }

    public fun beginUserAction() {
        gtk_text_buffer_begin_user_action(self)
    }

    public fun endUserAction() {
        gtk_text_buffer_end_user_action(self)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////
    public fun onChanged(callback: () -> Unit) {
        println("Connecting signal")
        self.connectSignal(
            "changed",
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    public fun onApplyTag(callback: (TextTag, start: TextIter, end: TextIter) -> Unit) {
        self.connectSignal(
            "apply-tag",
            handler = staticCallback3,
            callbackWrapper = StableRef.create { t: CPointer<GtkTextTag>, s: CPointer<GtkTextIter>, e: CPointer<GtkTextIter> ->
                callback(TextTag(t), TextIter(s), TextIter(e))
            }.asCPointer()
        )
    }

    public fun onRemoveTag(callback: (TextTag, start: TextIter, end: TextIter) -> Unit) {
        self.connectSignal(
            "remove-tag",
            handler = staticCallback3,
            callbackWrapper = StableRef.create { t: CPointer<GtkTextTag>, s: CPointer<GtkTextIter>, e: CPointer<GtkTextIter> ->
                callback(TextTag(t), TextIter(s), TextIter(e))
            }.asCPointer()
        )
    }

    public fun onBeginUserAction(callback: () -> Unit) {
        self.connectSignal(
            "begin-user-action",
            handler = staticCallback,
            StableRef.create(callback).asCPointer()
        )
    }

    public fun onEndUserAction(callback: () -> Unit) {
        self.connectSignal(
            "end-user-action",
            handler = staticCallback,
            StableRef.create(callback).asCPointer()
        )
    }

    public fun onDeleteRange(callback: (start: TextIter, end: TextIter) -> Unit) {
        self.connectSignal(
            "begin-user-action",
            handler = staticCallback2,
            StableRef.create { s: CPointer<GtkTextIter>, e: CPointer<GtkTextIter> ->
                callback(TextIter(s), TextIter(e))
            }.asCPointer()
        )
    }

    public fun onInsertChildAnchor(callback: (position: TextIter, anchor: TextChildAnchor) -> Unit) {
        self.connectSignal(
            "insert-child-anchor",
            handler = staticCallback2,
            StableRef.create { s: CPointer<GtkTextIter>, e: CPointer<GtkTextChildAnchor> ->
                callback(TextIter(s), TextChildAnchor(e))
            }.asCPointer()
        )
    }

    public fun onInsertPixbuf(callback: (location: TextIter, pixbuf: Pixbuf) -> Unit) {
        self.connectSignal(
            "insert-pixbuf",
            handler = staticCallback2,
            StableRef.create { s: CPointer<GtkTextIter>, e: CPointer<GdkPixbuf> ->
                callback(TextIter(s), Pixbuf(e))
            }.asCPointer()
        )
    }

    public fun onInsertText(callback: (location: TextIter, text: String) -> Unit) {
        self.connectSignal(
            "insert-text",
            handler = staticCallback3,
            StableRef.create { s: CPointer<GtkTextIter>, t: CPointer<ByteVar>, _: COpaquePointer? ->
                callback(TextIter(s), t.toKString())
            }.asCPointer()
        )
    }

    public fun onMarkDeleted(callback: (mark: TextMark) -> Unit) {
        self.connectSignal(
            "mark-deleted",
            handler = staticCallback1,
            StableRef.create { p: CPointer<GtkTextMark> ->
                callback(TextMark(p))
            }.asCPointer()
        )
    }

    public fun onMarkSet(callback: (location: TextIter, mark: TextMark) -> Unit) {
        self.connectSignal(
            "mark-set",
            handler = staticCallback2,
            StableRef.create { p: CPointer<GtkTextIter>, m: CPointer<GtkTextMark> ->
                callback(TextIter(p), TextMark(m))
            }.asCPointer()
        )
    }

    public fun onModifiedChanged(callback: (Boolean) -> Unit) {
        self.connectSignal(
            "modified-changed",
            handler = staticCallback,
            StableRef.create {
                callback(modified)
            }.asCPointer()
        )
    }

    public fun onPaste(callback: (Clipboard) -> Unit) {
        self.connectSignal(
            "paste-done",
            handler = staticCallback1,
            callbackWrapper = StableRef.create { c: CPointer<GtkClipboard> ->
                callback(Clipboard(c))
            }.asCPointer()
        )
    }
}

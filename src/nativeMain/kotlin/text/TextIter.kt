@file:Suppress("unused")

package ux

import libgtk.*

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString



public class TextIter(internal val self: CPointer<GtkTextIter>) {

    public enum class MoveBoundary {
        Char,
        Line,
        WordStart,
        WordEnd,
        Cursor,
        SentenceStart,
        SentenceEnd,
    }

    public val buffer: TextBuffer
        get() = TextBuffer(gtk_text_iter_get_buffer(self)!!)

    public var offset: Int
        get() = gtk_text_iter_get_offset(self)
        set(value) {
            gtk_text_iter_set_offset(self, value)
        }

    public var line: Int
        get() = gtk_text_iter_get_line(self)
        set(value) {
            gtk_text_iter_set_line(self, value)
        }

    public var lineOffset: Int
        get() = gtk_text_iter_get_line_offset(self)
        set(value) = gtk_text_iter_set_line_offset(self, value)

    public var visibleLineOffset: Int
        get() = gtk_text_iter_get_visible_line_offset(self)
        set(value) {
            gtk_text_iter_set_visible_line_offset(self, value)
        }

    public var lineIndex: Int
        get() = gtk_text_iter_get_line_index(self)
        set(value) {
            gtk_text_iter_set_line_index(self, value)
        }

    public var visibleLineIndex: Int
        get() = gtk_text_iter_get_visible_line_index(self)
        set(value) {
            gtk_text_iter_set_visible_line_index(self, value)
        }

    public val char: Char
        get() = gtk_text_iter_get_char(self).toByte().toChar()

    public val pixbuf: Pixbuf?
        get() = gtk_text_iter_get_pixbuf(self)?.let { Pixbuf(it) }

    public val childAnchor: TextChildAnchor?
        get() = gtk_text_iter_get_child_anchor(self)?.let { TextChildAnchor(it) }

    public val marks: Set<TextMark>
        get() {
            val cList = gtk_text_iter_get_marks(self) ?: return emptySet()
            return cList.asSequence<GtkTextMark>().map {
                TextMark(it)
            }.toSet()
        }

    public val tags: Set<TextTag>
        get() {
            val cList = gtk_text_iter_get_tags(self) ?: return emptySet()
            return cList.asSequence<GtkTextTag>().map {
                TextTag(it)
            }.toSet()
        }

    public val editable: Boolean
        get() = Boolean from gtk_text_iter_editable(self, 1)

    public val canInsert: Boolean
        get() = Boolean from gtk_text_iter_can_insert(self, 1)

    public val startsWord: Boolean
        get() = Boolean from gtk_text_iter_starts_word(self)

    public val endsWord: Boolean
        get() = Boolean from gtk_text_iter_ends_word(self)

    public val insideWord: Boolean
        get() = Boolean from gtk_text_iter_inside_word(self)

    public val startsLine: Boolean
        get() = Boolean from gtk_text_iter_starts_line(self)

    public val endsLine: Boolean
        get() = Boolean from gtk_text_iter_ends_line(self)

    public val startsSentence: Boolean
        get() = Boolean from gtk_text_iter_starts_sentence(self)

    public val endsSentence: Boolean
        get() = Boolean from gtk_text_iter_ends_sentence(self)

    public val insideSentence: Boolean
        get() = Boolean from gtk_text_iter_inside_sentence(self)

    public val isCursorPosition: Boolean
        get() = Boolean from gtk_text_iter_is_cursor_position(self)

    public val charsInLine: Int
        get() = gtk_text_iter_get_chars_in_line(self)

    public val bytesInLine: Int
        get() = gtk_text_iter_get_bytes_in_line(self)

    public val isEnd: Boolean
        get() = Boolean from gtk_text_iter_is_end(self)

    public val isStart: Boolean
        get() = Boolean from gtk_text_iter_is_start(self)

    public fun move(
        amount: Int = 1,
        boundary: MoveBoundary = MoveBoundary.Char,
        visibleOnly: Boolean = true
    ) {
        when (boundary) {
            MoveBoundary.Char -> {
                if (amount >= 0) {
                    gtk_text_iter_forward_chars(self, amount)
                } else {
                    gtk_text_iter_backward_chars(self, -amount)
                }
            }
            MoveBoundary.Line -> {
                if (amount >= 0) {
                    if (visibleOnly) {
                        gtk_text_iter_forward_visible_lines(self, amount)
                    } else {
                        gtk_text_iter_forward_lines(self, amount)
                    }
                } else {
                    if (visibleOnly) {
                        gtk_text_iter_forward_visible_lines(self, -amount)
                    } else {
                        gtk_text_iter_forward_lines(self, -amount)
                    }
                }
            }
            MoveBoundary.WordStart -> {
                require(amount <= 0) { "Word Start mode allows only backwards movement" }
                if (visibleOnly) {
                    gtk_text_iter_backward_visible_word_starts(self, -amount)
                } else {
                    gtk_text_iter_backward_word_starts(self, -amount)
                }

            }
            MoveBoundary.WordEnd -> {
                require(amount >= 0) { "Word Start mode allows only forwards movement" }
                if (visibleOnly) {
                    gtk_text_iter_forward_visible_word_ends(self, amount)
                } else {
                    gtk_text_iter_forward_word_ends(self, amount)
                }

            }
            MoveBoundary.Cursor -> {
                if (amount >= 0) {
                    if (visibleOnly) {
                        gtk_text_iter_forward_visible_cursor_positions(self, amount)
                    } else {
                        gtk_text_iter_forward_cursor_positions(self, amount)
                    }
                } else {
                    if (visibleOnly) {
                        gtk_text_iter_forward_visible_cursor_positions(self, -amount)
                    } else {
                        gtk_text_iter_forward_cursor_positions(self, -amount)
                    }
                }
            }
            MoveBoundary.SentenceStart -> {
                require(amount <= 0) { "Sentence Start mode allows only backwards movement" }
                gtk_text_iter_backward_sentence_starts(self, -amount)
            }
            MoveBoundary.SentenceEnd -> {
                require(amount >= 0) { "Sentence End mode allows only forwards movement" }
                gtk_text_iter_forward_sentence_ends(self, amount)
            }
        }
    }

    public fun moveToEnd() {
        gtk_text_iter_forward_to_end(self)
    }

    public fun moveToLineEnd() {
        gtk_text_iter_forward_to_line_end(self)
    }

    public fun moveToTagToggle(tag: TextTag? = null, backwards: Boolean = false) {
        if (backwards)
            gtk_text_iter_backward_to_tag_toggle(self, tag?.self)
        else
            gtk_text_iter_forward_to_tag_toggle(self, tag?.self)
    }

    public fun startsTag(tag: TextTag? = null): Boolean {
        return Boolean from gtk_text_iter_starts_tag(self, tag?.self)
    }

    public fun endsTag(tag: TextTag? = null): Boolean {
        return Boolean from gtk_text_iter_ends_tag(self, tag?.self)
    }

    public fun togglesTag(tag: TextTag? = null): Boolean {
        return Boolean from gtk_text_iter_toggles_tag(self, tag?.self)
    }

    public fun hasTag(tag: TextTag): Boolean {
        return Boolean from gtk_text_iter_has_tag(self, tag.self)
    }

    public fun getToggledTags(toggledOn: Boolean = true): Set<TextTag> {
        return gtk_text_iter_get_toggled_tags(self, toggledOn.gtkValue).asSequence<GtkTextTag>().map {
            TextTag(it)
        }.toSet()
    }

    // gtk_text_iter_get_slice but nicer
    public fun sliceUntil(end: TextIter, visibleOnly: Boolean = false): String {
        return if (visibleOnly)
            gtk_text_iter_get_visible_slice(self, end.self)?.toKString() ?: ""
        else
            gtk_text_iter_get_slice(self, end.self)?.toKString() ?: ""
    }

    public fun textUntil(end: TextIter, visibleOnly: Boolean = false): String {
        return if (visibleOnly)
            gtk_text_iter_get_visible_text(self, end.self)?.toKString() ?: ""
        else
            gtk_text_iter_get_text(self, end.self)?.toKString() ?: ""
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TextIter) return false

        return Boolean from gtk_text_iter_equal(self, other.self)
    }

    override fun hashCode(): Int {
        return self.pointed.hashCode()
    }
}

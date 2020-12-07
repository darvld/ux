@file:Suppress("unused")

package ux


import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret
import libgtk.*

public class TextTagTable(
    pointer: CPointer<GtkTextTagTable>
) {
    public constructor() : this(gtk_text_tag_table_new()!!)

    private val self: CPointer<GtkTextTagTable> = pointer.reinterpret()

    public val size: Int
        get() = gtk_text_tag_table_get_size(self)

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    public fun add(tag: TextTag) {
        gtk_text_tag_table_add(self, tag.self)
    }

    public fun remove(tag: TextTag) {
        gtk_text_tag_table_remove(self, tag.self)
    }

    public fun lookup(name: String): TextTag? {
        return gtk_text_tag_table_lookup(self, name)?.let { TextTag(it) }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////

    public fun onTagAdded(callback: (TextTag) -> Unit) {
        self.connectSignal(
            "tag-added",
            handler = staticCallback1,
            callbackWrapper = StableRef.create { void: COpaquePointer? ->
                callback(TextTag(void!!.reinterpret()))
            }.asCPointer()
        )
    }

    public fun onTagChanged(callback: (TextTag, sizeChanged: Boolean) -> Unit) {
        self.connectSignal(
            "tag-changed",
            handler = staticCallback2,
            callbackWrapper = StableRef.create { void: COpaquePointer?, bool: gboolean ->
                callback(TextTag(void!!.reinterpret()), Boolean from bool)
            }.asCPointer()
        )
    }

    public fun onTagRemoved(callback: (TextTag) -> Unit) {
        self.connectSignal(
            "tag-removed",
            handler = staticCallback1,
            callbackWrapper = StableRef.create { void: COpaquePointer? ->
                callback(TextTag(void!!.reinterpret()))
            }.asCPointer()
        )
    }
}

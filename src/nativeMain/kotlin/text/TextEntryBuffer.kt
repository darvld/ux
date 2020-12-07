@file:Suppress("unused")

package ux



import libgtk.*



import kotlinx.cinterop.*

public class EntryBuffer internal constructor(
    internal val self: CPointer<GtkEntryBuffer>
) : GObjectWrapper(self.reinterpret()) {
    public constructor(text: String? = null) : this(gtk_entry_buffer_new(text, text?.length ?: 0)!!)

    public var text: String
        get() = gtk_entry_buffer_get_text(self)!!.toKString()
        set(value) {
            gtk_entry_buffer_set_text(self, value, value.length)
        }

    public val bytes: Long
        get() = gtk_entry_buffer_get_bytes(self).convert()

    public val length: Int
        get() = gtk_entry_buffer_get_length(self).convert()

    public var maxLength: Int
        get() = gtk_entry_buffer_get_max_length(self)
        set(value) {
            gtk_entry_buffer_set_max_length(self, value)
        }

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    public fun insertText(text: String, position: Int) {
        gtk_entry_buffer_insert_text(self, position.convert(), text, text.length)
    }

    public fun deleteText(position: Int, amount: Int) {
        gtk_entry_buffer_delete_text(self, position.convert(), amount)
    }

    public inline fun deleteText(range: IntRange) {
        deleteText(range.first, range.last - range.first)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////
    public fun onTextDeleted(callback: (position: Int, amount: Int) -> Unit) {
        self.connectSignal(
            "deleted-text",
            handler = staticCallback2,
            callbackWrapper = StableRef.create { a: Int, b: Int ->
                callback(a, b)
            }.asCPointer()
        )
    }

    public fun onInsertedText(callback: (position: Int, text: String) -> Unit) {
        self.connectSignal(
            "inserted-text",
            handler = staticCallback3,
            callbackWrapper = StableRef.create { position: Int?, text: CPointer<ByteVar>, _: Void? ->
                callback(position ?: 0, text.toKString())
            }.asCPointer()
        )
    }
}

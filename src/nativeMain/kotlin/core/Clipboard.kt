@file:Suppress("unused")

package ux

import libgtk.*
import kotlinx.cinterop.CPointer

// TODO: 11/25/2020 Incomplete
public class Clipboard(internal val cpointer: CPointer<GtkClipboard>) {

    public companion object {
        public fun get(selection: Atom): Clipboard {
            return Clipboard(gtk_clipboard_get(selection)!!)
        }
    }

    public fun clear() {
        gtk_clipboard_clear(cpointer)
    }

    public fun setText(text: String) {
        gtk_clipboard_set_text(cpointer, text, text.length)
    }

    public fun setImage(image: Pixbuf) {
        gtk_clipboard_set_image(cpointer, image.cpointer)
    }


}

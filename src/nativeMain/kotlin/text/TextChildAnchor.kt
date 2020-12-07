package ux

import kotlinx.cinterop.CPointer
import libgtk.*

public class TextChildAnchor internal constructor(internal val self: CPointer<GtkTextChildAnchor>) {
    public constructor() : this(gtk_text_child_anchor_new()!!)

    public val widgets: List<Widget>
        get() {
            return gtk_text_child_anchor_get_widgets(self).asSequence<GtkWidget>().map {
                Widget(it)
            }.toList()
        }

    public val deleted: Boolean
        get() = Boolean from gtk_text_child_anchor_get_deleted(self)
}

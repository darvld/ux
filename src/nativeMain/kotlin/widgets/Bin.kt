package ux

import libgtk.GtkBin
import libgtk.GtkWidget
import libgtk.gtk_bin_get_child
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

public open class Bin internal constructor(pointer: CPointer<GtkWidget>) : Container(pointer) {
    private val self: CPointer<GtkBin> = pointer.reinterpret()

    public val child: Widget?
        get() = gtk_bin_get_child(self)?.let { Widget(it) }

}

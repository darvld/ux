@file:Suppress("unused")

package ux

import libgtk.GtkCellRenderer
import libgtk.gtk_cell_renderer_text_new
import kotlinx.cinterop.CPointer

public open class TextCellRenderer(pointer: CPointer<GtkCellRenderer>) :
    CellRenderer(pointer) {
    public constructor() : this(gtk_cell_renderer_text_new()!!)
}

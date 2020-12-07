package ux

import libgtk.GtkCellRenderer
import kotlinx.cinterop.CPointer

public open class CellRenderer(internal val cellPointer: CPointer<GtkCellRenderer>)

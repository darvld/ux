package ux

import libgtk.gtk_separator_new
import ux.Orientation
import ux.Widget
import ux.WidgetPtr

public class Separator(pointer: WidgetPtr) : Widget(pointer) {
    public constructor(orientation: Orientation) : this(gtk_separator_new(orientation.gtkValue)!!)
}

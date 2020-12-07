@file:Suppress("unused")

package ux

import libgtk.*


import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret

public interface Scrollable : ExtensionInterface

public var Scrollable.horizontalAdjustment: Adjustment
    get() = Adjustment(gtk_scrollable_get_hadjustment(baseWidget.widgetPtr.reinterpret())!!)
    set(value) {
        gtk_scrollable_set_hadjustment(baseWidget.widgetPtr.reinterpret(), value.self)
    }

public var Scrollable.verticalAdjustment: Adjustment
    get() = Adjustment(gtk_scrollable_get_vadjustment(baseWidget.widgetPtr.reinterpret())!!)
    set(value) {
        gtk_scrollable_set_vadjustment(baseWidget.widgetPtr.reinterpret(), value.self)
    }

public var Scrollable.horizontalScrollPolicy: ScrollablePolicy
    get() {
        return ScrollablePolicy from gtk_scrollable_get_hscroll_policy(baseWidget.widgetPtr.reinterpret())
    }
    set(value) {
        gtk_scrollable_set_hscroll_policy(baseWidget.widgetPtr.reinterpret(),value.gtkValue)
    }

public var Scrollable.verticalScrollPolicy: ScrollablePolicy
    get() {
        return ScrollablePolicy from gtk_scrollable_get_vscroll_policy(baseWidget.widgetPtr.reinterpret())
    }
    set(value) {
        gtk_scrollable_set_vscroll_policy(baseWidget.widgetPtr.reinterpret(),value.gtkValue)
    }



public enum class ScrollablePolicy {
    Minimum,
    Natural;

    internal inline val gtkValue: GtkScrollablePolicy get() = ordinal.convert()

    internal companion object {
        internal infix fun from(gtkValue: GtkScrollablePolicy): ScrollablePolicy = values()[gtkValue.convert()]
    }
}

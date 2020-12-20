@file:Suppress("unused")

package ux

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import libgtk.*

public class Expander(widgetPtr: WidgetPtr) : Bin(widgetPtr) {
    private val self: CPointer<GtkExpander> = widgetPtr.reinterpret()

    public constructor(label: String? = null) : this(gtk_expander_new(label)!!)

    public var expanded: Boolean
        get() = Boolean from gtk_expander_get_expanded(self)
        set(value) {
            gtk_expander_set_expanded(self, value.gtkValue)
        }

    public var label: String?
        get() = gtk_expander_get_label(self)?.toKString()
        set(value) {
            gtk_expander_set_label(self, value)
        }

    public var useUnderline: Boolean
        get() = Boolean from gtk_expander_get_use_underline(self)
        set(value) {
            gtk_expander_set_use_underline(self, value.gtkValue)
        }

    public var useMarkup: Boolean
        get() = Boolean from gtk_expander_get_use_markup(self)
        set(value) {
            gtk_expander_set_use_markup(self, value.gtkValue)
        }

    public var labelWidget: Widget?
        get() = gtk_expander_get_label_widget(self)?.let { Widget(it) }
        set(value) {
            gtk_expander_set_label_widget(self, value?.widgetPtr)
        }

    public var labelFill: Boolean
        get() = Boolean from gtk_expander_get_label_fill(self)
        set(value) {
            gtk_expander_set_label_fill(self, value.gtkValue)
        }

    public var resizeToplevel: Boolean
        get() = Boolean from gtk_expander_get_resize_toplevel(self)
        set(value) {
            gtk_expander_set_resize_toplevel(self, value.gtkValue)
        }


    public fun onActivate(callback: () -> Unit) {
        connectSignal(
            "activate",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }
}

@WidgetDsl
public fun Container.expander(
    label: String? = null,
    op: Expander.() -> Unit = {},
): Expander {
    return Expander(label).also { instance ->
        add(instance)
        instance.show()

        instance.op()
    }
}

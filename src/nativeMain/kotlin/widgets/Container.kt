package ux

import libgtk.*


import kotlinx.cinterop.*

public open class Container internal constructor(pointer: CPointer<GtkWidget>) : Widget(pointer) {

    private val self: CPointer<GtkContainer> = pointer.reinterpret()

    public fun add(widget: Widget) {
        gtk_container_add(self, widget.widgetPtr)
    }

    public fun remove(widget: Widget) {
        gtk_container_remove(self, widget.widgetPtr)
    }

    internal fun getChildProperty(child: Widget, property: String): GValueWrapper = memScoped {
        val pointer = alloc<GValue>().ptr
        gtk_container_child_get_property(self, child.widgetPtr, property, pointer)

        return GValueWrapper(pointer)
    }

    internal inline fun <T> setChildProperty(child: Widget, property: String, value: T) = memScoped {
        val pointer = gvalue(value).pointer
        gtk_container_child_set_property(self, child.widgetPtr, property, pointer)
    }
}

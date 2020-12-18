@file:Suppress("unused")

package ux


import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import libgtk.*
import ux.Container
import ux.Widget
import ux.from
import ux.gtkValue

public open class Stack(
    pointer: WidgetPtr
) : Container(pointer) {
    public constructor() : this(gtk_stack_new()!!)

    private val self: CPointer<GtkStack> = pointer.reinterpret()

    public fun addNamed(child: Widget, name: String, title: String? = null) {
        title?.let {
            gtk_stack_add_titled(self, child.widgetPtr, name, it)
        }
        gtk_stack_add_named(self, child.widgetPtr, name)
    }

    public fun getChildByName(name: String): Widget? {
        return gtk_stack_get_child_by_name(self, name)?.let { Widget(it) }
    }

    public var visibleChild: Widget?
        get() = gtk_stack_get_visible_child(self)?.let { Widget(it) }
        set(value) {
            gtk_stack_set_visible_child(self, value?.widgetPtr)
        }

    public var visibleChildName: String?
        get() = gtk_stack_get_visible_child_name(self)?.toKString()
        set(value) {
            gtk_stack_set_visible_child_name(self, value)
        }

    public var homogeneous: Boolean
        get() = Boolean from gtk_stack_get_homogeneous(self)
        set(value) {
            gtk_stack_set_homogeneous(self, value.gtkValue)
        }

    public var hHomogeneous: Boolean
        get() = Boolean from gtk_stack_get_hhomogeneous(self)
        set(value) {
            gtk_stack_set_hhomogeneous(self, value.gtkValue)
        }

    public var vHomogeneous: Boolean
        get() = Boolean from gtk_stack_get_vhomogeneous(self)
        set(value) {
            gtk_stack_set_vhomogeneous(self, value.gtkValue)
        }

    @ExperimentalUnsignedTypes
    public var transitionDuration: UInt
        get() = gtk_stack_get_transition_duration(self)
        set(value) {
            gtk_stack_set_transition_duration(self, value)
        }

    public var transitionType: GtkStackTransitionType
        get() = gtk_stack_get_transition_type(self)
        set(value) {
            gtk_stack_set_transition_type(self, value)
        }

    public val transitionRunning: Boolean
        get() = Boolean from gtk_stack_get_transition_running(self)

    public var interpolateSize: Boolean
        get() = Boolean from gtk_stack_get_interpolate_size(self)
        set(value) {
            gtk_stack_set_interpolate_size(self, value.gtkValue)
        }
    
    public fun setVisibleChild(name: String, transition: GtkStackTransitionType) {
        gtk_stack_set_visible_child_full(self, name, transition)
    }
}

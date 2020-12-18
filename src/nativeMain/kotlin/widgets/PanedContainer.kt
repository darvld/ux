@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ux



import libgtk.*

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

public open class PanedContainer(
    pointer: WidgetPtr
) : Container(pointer) {
    public constructor(orientation: Orientation = Orientation.Horizontal) : this(gtk_paned_new(orientation.gtkValue)!!)

    private val self: CPointer<GtkPaned> = pointer.reinterpret()

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////

    public val firstChild: Widget?
        get() = gtk_paned_get_child1(self)?.let { Widget(it) }

    public val secondChild: Widget?
        get() = gtk_paned_get_child2(self)?.let { Widget(it) }

    public var dividerPosition: Int
        get() = gtk_paned_get_position(self)
        set(value) {
            gtk_paned_set_position(self, value)
        }

    public var useWideHandle: Boolean
        get() = Boolean from gtk_paned_get_wide_handle(self)
        set(value) {
            gtk_paned_set_wide_handle(self, value.gtkValue)
        }

    ///////////////////////////////////////////////////////////////////////////
    // GObject properties
    ///////////////////////////////////////////////////////////////////////////

    public val maxPosition: Int by obj.gProperty("max-position")
    public val minPosition: Int by obj.gProperty("min-position")

    public val handleSize: Int by obj.gProperty("handle-size")

    ///////////////////////////////////////////////////////////////////////////
    // Extension properties (for child widgets)
    ///////////////////////////////////////////////////////////////////////////

    public var Widget.resize: Boolean
        get() = this@PanedContainer.getChildProperty(this, "resize").asBoolean
        set(value) = this@PanedContainer.setChildProperty(this, "resize", value)

    public var Widget.shrink: Boolean
        get() = this@PanedContainer.getChildProperty(this, "shrink").asBoolean
        set(value) = this@PanedContainer.setChildProperty(this, "shrink", value)

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    public fun addFirst(widget: Widget) {
        gtk_paned_add1(self, widget.widgetPtr)
    }

    public fun packFirst(widget: Widget, resize: Boolean = true, shrink: Boolean = false) {
        gtk_paned_pack1(self, widget.widgetPtr, resize.gtkValue, shrink.gtkValue)
    }

    public fun addSecond(widget: Widget) {
        gtk_paned_add2(self, widget.widgetPtr)
    }

    public fun packSecond(widget: Widget, resize: Boolean = true, shrink: Boolean = false) {
        gtk_paned_pack2(self, widget.widgetPtr, resize.gtkValue, shrink.gtkValue)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Extra builder methods
    ///////////////////////////////////////////////////////////////////////////
    public fun <T : Widget> firstPanel(child: T, op: T.() -> Unit) {
        packFirst(child)
        child.resize = true
        child.op()
    }

    public fun <T : Widget> secondPanel(child: T, op: T.() -> Unit) {
        packSecond(child)
        child.resize = true
        child.op()
    }
}

@WidgetDsl
public fun Container.panedContainer(
    orientation: Orientation = Orientation.Horizontal,
    op: PanedContainer .() -> Unit,
): PanedContainer {
    return PanedContainer(orientation).also { instance ->
        add(instance)
        instance.show()

        instance.op()
    }
}

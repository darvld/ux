@file:Suppress("unused")

package ux

import libgtk.*
import kotlinx.cinterop.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


public open class Box internal constructor(pointer: WidgetPtr) : Container(pointer) {
    public constructor(orientation: Orientation = Orientation.Horizontal, spacing: Int = 0) :
            this(gtk_box_new(orientation.gtkValue, spacing)!!)


    private val self: CPointer<GtkBox> = pointer.reinterpret()

    public var homogeneous: Boolean
        get() = Boolean from gtk_box_get_homogeneous(self)
        set(value) {
            gtk_box_set_homogeneous(self, value.gtkValue)
        }

    public var spacing: Int
        get() = gtk_box_get_spacing(self)
        set(value) {
            gtk_box_set_spacing(self, value)
        }

    public fun reorderChild(child: Widget, position: Int) {
        gtk_box_reorder_child(self, child.widgetPtr, position)
    }

    public fun packStart(
        widget: Widget,
        expand: Boolean = false,
        fill: Boolean = false,
        padding: Int = 0
    ) {
        gtk_box_pack_start(self, widget.widgetPtr, expand.gtkValue, fill.gtkValue, padding.convert())
    }

    public fun packEnd(
        widget: Widget,
        expand: Boolean = false,
        fill: Boolean = false,
        padding: Int = 0
    ) {
        gtk_box_pack_end(self, widget.widgetPtr, expand.gtkValue, fill.gtkValue, padding.convert())
    }



    public fun setChildPacking(child: Widget, packing: Packing) {
        gtk_box_set_child_packing(
            self,
            child.widgetPtr,
            packing.expand.gtkValue,
            packing.fill.gtkValue,
            packing.padding.convert(),
            packing.type.gtkValue
        )
    }

    public var Widget.packing: Packing
        get() {
            memScoped {
                val expandValue = alloc<gbooleanVar>()
                val fillValue = alloc<gbooleanVar>()
                val paddingValue = alloc<guintVar>()
                val typeValue = alloc<GtkPackType.Var>()

                gtk_box_query_child_packing(
                    this@Box.self,
                    this@packing.widgetPtr,
                    expandValue.ptr,
                    fillValue.ptr,
                    paddingValue.ptr,
                    typeValue.ptr
                )

                return Packing(
                    PackType from typeValue.value,
                    Boolean from expandValue.value,
                    Boolean from fillValue.value,
                    paddingValue.value.convert(),
                )
            }
        }
        set(value) {
            this@Box.setChildPacking(this, Packing(value.type, value.expand, value.fill, value.padding))
        }

    public fun Widget.setPacking(type: GtkPackType, expand: Boolean = false, fill: Boolean = false, padding: Int = 0) {
        this@Box.setChildPacking(this, Packing(PackType.from(type), expand, fill, padding))
    }
}

@WidgetDsl
public fun Container.box(
    orientation: Orientation = Orientation.Vertical,
    spacing: Int = 12,
    op: Box.() -> Unit = {},
): Box {
    return Box(orientation, spacing).also { instance ->
        add(instance)
        instance.show()

        instance.op()
    }
}

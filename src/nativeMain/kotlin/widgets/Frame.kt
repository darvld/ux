package ux

import kotlinx.cinterop.*
import libgtk.*

public class Frame(pointer: WidgetPtr) : Bin(pointer) {
    public constructor(label: String? = null) : this(gtk_frame_new(label)!!)

    private val self: CPointer<GtkFrame> = pointer.reinterpret()

    public var label: String?
        get() = gtk_frame_get_label(self)?.toKString()
        set(value) {
            gtk_frame_set_label(self, value)
        }

    public var labelWidget: Widget?
        get() = gtk_frame_get_label_widget(self)?.let { Widget(it) }
        set(value) {
            gtk_frame_set_label_widget(self, value?.widgetPtr)
        }

    public var labelAlign: Pair<Float, Float>
        get() = memScoped {
            val x = alloc<FloatVar>()
            val y = alloc<FloatVar>()

            gtk_frame_get_label_align(self, x.ptr, y.ptr)
            x.value
            return x.value to y.value
        }
        set(value) {
            gtk_frame_set_label_align(self, value.first, value.second)
        }

    public var shadowType: ShadowType
        get() = ShadowType from gtk_frame_get_shadow_type(self)
        set(value) {
            gtk_frame_set_shadow_type(self, value.gtkValue)
        }
}

public fun Container.frame(label: String? = null, setup: Frame.() -> Unit): Frame {
    val widget = Frame(label)
    add(widget)

    widget.show()
    widget.setup()

    return widget
}

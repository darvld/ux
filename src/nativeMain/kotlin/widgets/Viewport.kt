@file:Suppress("unused")

package ux

import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret


public class Viewport internal constructor(
    pointer: CPointer<GtkWidget>
) : Bin(pointer), Scrollable {
    public constructor() : this(gtk_viewport_new(null, null)!!)
    override val baseWidget: Widget by lazy { this }

    private val self: CPointer<GtkViewport> = pointer.reinterpret()

    public var shadowType: ShadowType
        get() = ShadowType from gtk_viewport_get_shadow_type(self)
        set(value) {
            gtk_viewport_set_shadow_type(self, value.gtkValue)
        }
}

@WidgetDsl
public inline fun Container.viewport(
    op: Viewport.() -> Unit = {}
): Viewport {
    val v = Viewport()

    add(v)
    v.show()
    v.op()
    return v
}


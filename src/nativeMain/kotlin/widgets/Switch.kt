package ux



import kotlinx.cinterop.*
import libgtk.*

public open class Switch(
    pointer: WidgetPtr
) : Widget(pointer) {
    public constructor() : this(gtk_switch_new()!!)

    private val self: CPointer<GtkSwitch> = pointer.reinterpret()

    public var active: Boolean
        get() = Boolean from gtk_switch_get_active(self)
        set(value) {
            gtk_switch_set_active(self, value.gtkValue)
        }

    public var state: Boolean
        get() = Boolean from gtk_switch_get_state(self)
        set(value) {
            gtk_switch_set_state(self, value.gtkValue)
        }

    public fun onStateSet(callback: (Boolean) -> Boolean) {
        connectSignal(
            "state-set",
            handler = stateSetSignalHandler,
            StableRef.create(callback).asCPointer()
        )
    }
}

private val stateSetSignalHandler: GCallback = staticCFunction { _: Void?, state: gboolean, callback: Void? ->
    val result = callback?.asStableRef<(Boolean) -> Boolean>()?.get()?.invoke(state == 1)

    return@staticCFunction result?.gtkValue ?: 0
}.reinterpret()

@WidgetDsl
public fun Container.switch(
    setup: Switch.() -> Unit = {}
): Switch {
    return Switch().apply {
        this@switch.add(this)
        hAlign = Align.Center
        show()
        setup()
    }
}

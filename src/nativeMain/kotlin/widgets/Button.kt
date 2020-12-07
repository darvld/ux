package ux

import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString


public open class Button internal constructor(pointer: CPointer<GtkWidget>) : Bin(pointer) {
    public constructor() : this(gtk_button_new()!!)
    public constructor(label: String? = null) : this(gtk_button_new_with_label(label)!!)

    private val self: CPointer<GtkButton> = pointer.reinterpret()

    public var reliefStyle: ReliefStyle
        get() = ReliefStyle from gtk_button_get_relief(self)
        set(value) {
            gtk_button_set_relief(self, value.gtkValue)
        }

    public var label: String
        get() = gtk_button_get_label(self)?.toKString() ?: ""
        set(value) {
            gtk_button_set_label(self, value)
        }

    public var useUnderline: Boolean
        get() = Boolean from gtk_button_get_use_underline(self)
        set(value) {
            gtk_button_set_use_underline(self, value.gtkValue)
        }

    public var image: Widget?
        get() = gtk_button_get_image(self)?.let { Widget(it) }
        set(value) {
            gtk_button_set_image(self, value?.widgetPtr)
        }

    public var imagePosition: PositionType
        get() = PositionType from gtk_button_get_image_position(self)
        set(value) {
            gtk_button_set_image_position(self, value.gtkValue)
        }

    public var alwaysShowImage: Boolean
        get() = Boolean from gtk_button_get_always_show_image(self)
        set(value) {
            gtk_button_set_always_show_image(self, value.gtkValue)
        }

    /**Triggers the "clicked" signal core.from this button.*/
    public fun clicked() {
        gtk_button_clicked(self)
    }


    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////
    public fun onClick(callback: () -> Unit) {
        connectSignal(
            "clicked",
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }
}

@WidgetDsl
public fun Container.button(
    label: String? = null,
    op: Button.() -> Unit = {}
): Button {
    val b = label?.let { Button(it) } ?: Button()

    add(b)
    b.show()

    b.op()
    return b
}

@file:Suppress("unused")

package ux

import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret

public open class ButtonBox internal constructor(pointer: WidgetPtr) : Box(pointer) {
    public constructor(orientation: Orientation) : this(gtk_button_box_new(orientation.gtkValue)!!)

    private val self: CPointer<GtkButtonBox> = pointer.reinterpret()

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    /**Changes the way buttons are arranged in their container.*/
    public var layout: Style
        get() = Style from gtk_button_box_get_layout(self)
        set(value) {
            gtk_button_box_set_layout(self, value.gtkValue)
        }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////
    /**Used to dictate the style that a [ButtonBox] uses to layout the buttons it contains.*/
    public enum class Style {
        /**Buttons are evenly spread across the box.*/
        Spread,

        /**Buttons are placed at the edges of the box.*/
        Edge,

        /**Buttons are grouped towards the start of the box, (on the left for a HBox, or the top for a VBox).*/
        Start,

        /**Buttons are grouped towards the end of the box, (on the right for a HBox, or the bottom for a VBox).*/
        End,

        /**Buttons are centered in the box.*/
        Center,

        /**Buttons expand to fill the box. This entails giving buttons a "linked" appearance, making button sizes
         *  homogeneous, and setting spacing to 0 (same as setting [Box.homogeneous] to true and [Box.spacing] to 0 manually).*/
        Expand
        ;

        public val gtkValue: UInt = (ordinal + 1).convert()

        public companion object {
            public infix fun from(gtkValue: GtkButtonBoxStyle): Style {
                return values()[gtkValue.toInt() - 1]
            }
        }
    }
}


@WidgetDsl
public fun Container.buttonBox(
    orientation: Orientation = Orientation.Horizontal,
    op: ButtonBox.() -> Unit = {}
): ButtonBox {
    val b = ButtonBox(orientation)

    add(b)
    b.show()
    b.op()
    return b
}

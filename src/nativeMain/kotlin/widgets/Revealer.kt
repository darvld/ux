package ux

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import libgtk.*

public class Revealer(widgetPtr: WidgetPtr) : Bin(widgetPtr) {
    private val self: CPointer<GtkRevealer> = widgetPtr.reinterpret()

    public constructor() : this(gtk_revealer_new()!!)

    public var revealChild: Boolean
        get() = Boolean from gtk_revealer_get_reveal_child(self)
        set(value) {
            gtk_revealer_set_reveal_child(self, value.gtkValue)
        }

    public val childRevealed: Boolean
        get() = Boolean from gtk_revealer_get_child_revealed(self)

    @ExperimentalUnsignedTypes
    public var transitionDuration: UInt
        get() = gtk_revealer_get_transition_duration(self)
        set(value) {
            gtk_revealer_set_transition_duration(self, value)
        }

    public var transitionType: TransitionType
        get() = TransitionType from gtk_revealer_get_transition_type(self)
        set(value) {
            gtk_revealer_set_transition_type(self, value.gtkValue)
        }



    public enum class TransitionType {
        NONE,
        CROSSFADE,
        SLIDE_RIGHT,
        SLIDE_LEFT,
        SLIDE_UP,
        SLIDE_DOWN;

        internal inline val gtkValue: GtkRevealerTransitionType get() = GtkRevealerTransitionType.values()[ordinal]

        internal companion object {
            internal inline infix fun from(gtkValue: GtkRevealerTransitionType) = values()[gtkValue.ordinal]
        }
    }
}

@WidgetDsl
public fun Container.revealer(
    op: Revealer.() -> Unit = {},
): Revealer {
    return Revealer().also { instance ->
        add(instance)
        instance.show()

        instance.op()
    }
}

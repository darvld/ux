@file:Suppress("unused")

package ux


import libgtk.*

import kotlinx.cinterop.*

public class ScrollView internal constructor(
    pointer: CPointer<GtkWidget>
) : Bin(pointer) {
    public constructor() : this(gtk_scrolled_window_new(null, null)!!)

    private val self: CPointer<GtkScrolledWindow> = pointer.reinterpret()

    public var hAdjustment: Adjustment
        get() = Adjustment(gtk_scrolled_window_get_hadjustment(self)!!)
        set(value) {
            gtk_scrolled_window_set_hadjustment(self, value.self)
        }

    public var vAdjustment: Adjustment
        get() = Adjustment(gtk_scrolled_window_get_vadjustment(self)!!)
        set(value) {
            gtk_scrolled_window_set_vadjustment(self, value.self)
        }

    public val hScrollbar: Widget
        get() = Widget(gtk_scrolled_window_get_hscrollbar(self)!!)

    public val vScrollbar: Widget
        get() = Widget(gtk_scrolled_window_get_vscrollbar(self)!!)

    // TODO: 11/27/2020 Compiler bug prevents this code from being compiled, find out why and report the bug
    /*private val _hPolicy: CPointer<GtkPolicyType.Var> = nativeHeap.alloc()
    public val horizontalPolicy: Policy
        get() {
            gtk_scrolled_window_get_policy(self, _hPolicy, null)
            return Policy from _hPolicy.pointed.value
        }

    private val _vPolicy: CPointer<GtkPolicyType.Var> = nativeHeap.alloc()
    public val verticalPolicy: Policy
        get() {
            gtk_scrolled_window_get_policy(self, null, _vPolicy)
            return Policy from _vPolicy.pointed.value
        }*/

    public var placement: CornerType
        get() = CornerType from gtk_scrolled_window_get_placement(self)
        set(value) {
            gtk_scrolled_window_set_placement(self, value.gtkValue)
        }

    public var shadowType: ShadowType
        get() = ShadowType from gtk_scrolled_window_get_shadow_type(self)
        set(value) {
            gtk_scrolled_window_set_shadow_type(self, value.gtkValue)
        }

    public var kineticScrolling: Boolean
        get() = Boolean from gtk_scrolled_window_get_kinetic_scrolling(self)
        set(value) {
            gtk_scrolled_window_set_kinetic_scrolling(self, value.gtkValue)
        }

    public var captureButtonPress: Boolean
        get() = Boolean from gtk_scrolled_window_get_capture_button_press(self)
        set(value) {
            gtk_scrolled_window_set_capture_button_press(self, value.gtkValue)
        }

    public var overlayScrolling: Boolean
        get() = Boolean from gtk_scrolled_window_get_overlay_scrolling(self)
        set(value) {
            gtk_scrolled_window_set_overlay_scrolling(self, value.gtkValue)
        }

    public var minContentWidth: Int
        get() = gtk_scrolled_window_get_min_content_width(self)
        set(value) {
            gtk_scrolled_window_set_min_content_width(self, value)
        }

    public var minContentHeight: Int
        get() = gtk_scrolled_window_get_min_content_height(self)
        set(value) {
            gtk_scrolled_window_set_min_content_height(self, value)
        }

    public var maxContentWidth: Int
        get() = gtk_scrolled_window_get_max_content_width(self)
        set(value) {
            gtk_scrolled_window_set_max_content_width(self, value)
        }

    public var maxContentHeight: Int
        get() = gtk_scrolled_window_get_max_content_height(self)
        set(value) {
            gtk_scrolled_window_set_max_content_height(self, value)
        }

    public var propagateNaturalWidth: Boolean
        get() = Boolean from gtk_scrolled_window_get_propagate_natural_width(self)
        set(value) {
            gtk_scrolled_window_set_propagate_natural_width(self, value.gtkValue)
        }

    public var propagateNaturalHeight: Boolean
        get() = Boolean from gtk_scrolled_window_get_propagate_natural_height(self)
        set(value) {
            gtk_scrolled_window_set_propagate_natural_height(self, value.gtkValue)
        }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////

    public fun onEdgeOvershot(callback: (PositionType) -> Unit) {
        connectSignal(
            "edge-overshot",
            handler = staticCallback1,
            callbackWrapper = StableRef.create { ptr: CPointer<GtkPositionType.Var> ->
                callback(PositionType from ptr.pointed.value)
            }.asCPointer()
        )
    }

    public fun onEdgeReached(callback: (PositionType) -> Unit) {
        connectSignal(
            "edge-reached",
            handler = staticCallback1,
            callbackWrapper = StableRef.create { ptr: CPointer<GtkPositionType.Var> ->
                callback(PositionType from ptr.pointed.value)
            }.asCPointer()
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // Enums
    ///////////////////////////////////////////////////////////////////////////
    public enum class Policy {
        Always,
        Automatic,
        Never,
        External;

        internal inline val gtkValue: GtkPolicyType get() = GtkPolicyType.values()[ordinal]

        internal companion object {
            internal infix fun from(gtkValue: GtkPolicyType): Policy = values()[gtkValue.ordinal]
        }
    }

    public enum class CornerType {
        TopLeft,
        BottomLeft,
        TopRight,
        BottomRight;

        internal inline val gtkValue: GtkCornerType get() = GtkCornerType.values()[ordinal]

        internal companion object {
            internal infix fun from(gtkValue: GtkCornerType): CornerType = values()[gtkValue.ordinal]
        }
    }
}

@WidgetDsl
public inline fun Container.scrollView(
    op: ScrollView.() -> Unit = {}
): ScrollView {
    val v = ScrollView()

    add(v)
    v.show()
    v.op()
    return v
}

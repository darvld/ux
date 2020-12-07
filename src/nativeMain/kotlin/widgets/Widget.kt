@file:Suppress("MemberVisibilityCanBePrivate")

package ux


import kotlinx.cinterop.*
import libgtk.*

@WidgetDsl
public open class Widget internal constructor(internal val widgetPtr: CPointer<GtkWidget>) :
    GObjectWrapper(widgetPtr.reinterpret()) {

    public fun addStyleClass(name: String) {
        val style = gtk_widget_get_style_context(widgetPtr) ?: return

        gtk_style_context_add_class(style, name)
    }

    public fun removeStyleClass(name: String) {
        val style = gtk_widget_get_style_context(widgetPtr) ?: return

        gtk_style_context_remove_class(style, name)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    internal val obj: GObjectWrapper by lazy { GObjectWrapper(widgetPtr.reinterpret()) }

    public var canDefault: Boolean
        get() = Boolean from gtk_widget_get_can_default(widgetPtr)
        set(value) {
            gtk_widget_set_can_default(widgetPtr, value.gtkValue)
        }

    public var canFocus: Boolean
        get() = Boolean from gtk_widget_get_can_focus(widgetPtr)
        set(value) {
            gtk_widget_set_can_focus(widgetPtr, value.gtkValue)
        }

    public var events: EventMask
        get() = EventMask from gtk_widget_get_events(widgetPtr)
        set(value) {
            gtk_widget_set_events(widgetPtr, value.gtkValue.convert())
        }


    public var hExpand: Boolean
        get() = Boolean from gtk_widget_get_hexpand(widgetPtr)
        set(value) {
            gtk_widget_set_hexpand(widgetPtr, value.gtkValue)
        }

    public var vExpand: Boolean
        get() = Boolean from gtk_widget_get_vexpand(widgetPtr)
        set(value) {
            gtk_widget_set_vexpand(widgetPtr, value.gtkValue)
        }

    public var expand: Boolean
        get() = hExpand && vExpand
        set(value) {
            hExpand = value
            vExpand = value
        }

    public var focusOnClick: Boolean
        get() = Boolean from gtk_widget_get_focus_on_click(widgetPtr)
        set(value) {

            gtk_widget_set_focus_on_click(widgetPtr, value.gtkValue)
        }

    public var hAlign: Align
        get() = Align from gtk_widget_get_halign(widgetPtr)
        set(value) {
            gtk_widget_set_halign(widgetPtr, value.gtkValue)
        }

    public var vAlign: Align
        get() = Align from gtk_widget_get_valign(widgetPtr)
        set(value) {
            gtk_widget_set_valign(widgetPtr, value.gtkValue)
        }

    public val hasDefault: Boolean
        get() = Boolean from gtk_widget_has_default(widgetPtr)

    public val hasFocus: Boolean
        get() = Boolean from gtk_widget_has_focus(widgetPtr)

    public var hasTooltip: Boolean
        get() = Boolean from gtk_widget_get_has_tooltip(widgetPtr)
        set(value) {
            gtk_widget_set_has_tooltip(widgetPtr, value.gtkValue)
        }

    public var requestedWidth: Int
        get() = memScoped {
            val x = alloc<gintVar>()
            gtk_widget_get_size_request(widgetPtr, x.ptr, null)

            return x.value
        }
        set(value) {
            gtk_widget_set_size_request(widgetPtr, value, requestedHeight)
        }

    public var requestedHeight: Int
        get() = memScoped {
            val y = alloc<gintVar>()
            gtk_widget_get_size_request(widgetPtr, null, y.ptr)

            return y.value
        }
        set(value) {
            gtk_widget_set_size_request(widgetPtr, requestedWidth, value)
        }

    public var requestedSize: Vector
        get() = memScoped {
            val x = alloc<gintVar>()
            val y = alloc<gintVar>()
            gtk_widget_get_size_request(widgetPtr, x.ptr, y.ptr)

            return Vector(x.value, y.value)
        }
        set(value) {
            gtk_widget_set_size_request(widgetPtr, value.x, value.y)
        }

    public val isFocus: Boolean
        get() = Boolean from gtk_widget_is_focus(widgetPtr)

    public var margin: Int
        get() = maxOf(marginBottom, marginEnd, marginStart, marginTop)
        set(value) {
            marginBottom = value
            marginEnd = value
            marginStart = value
            marginTop = value
        }

    public var marginBottom: Int
        get() = gtk_widget_get_margin_bottom(widgetPtr)
        set(value) {
            gtk_widget_set_margin_bottom(widgetPtr, value)
        }

    public var marginEnd: Int
        get() = gtk_widget_get_margin_end(widgetPtr)
        set(value) {
            gtk_widget_set_margin_end(widgetPtr, value)
        }

    public var marginStart: Int
        get() = gtk_widget_get_margin_start(widgetPtr)
        set(value) {
            gtk_widget_set_margin_start(widgetPtr, value)
        }

    public var marginTop: Int
        get() = gtk_widget_get_margin_top(widgetPtr)
        set(value) {
            gtk_widget_set_margin_top(widgetPtr, value)
        }

    public var name: String?
        get() = gtk_widget_get_name(widgetPtr)?.toKString()
        set(value) {
            gtk_widget_set_name(widgetPtr, value)
        }

    public var opacity: Double
        get() = gtk_widget_get_opacity(widgetPtr)
        set(value) {
            gtk_widget_set_opacity(widgetPtr, value)
        }

    public var receivesDefault: Boolean
        get() = Boolean from gtk_widget_get_receives_default(widgetPtr)
        set(value) {
            gtk_widget_set_receives_default(widgetPtr, value.gtkValue)
        }

    public val scaleFactor: Int
        get() = gtk_widget_get_scale_factor(widgetPtr)

    public var sensitive: Boolean
        get() = Boolean from gtk_widget_get_sensitive(widgetPtr)
        set(value) {
            gtk_widget_set_sensitive(widgetPtr, value.gtkValue)
        }

    public var tooltipMarkup: String?
        get() = gtk_widget_get_tooltip_markup(widgetPtr)?.toKString()
        set(value) {
            gtk_widget_set_tooltip_markup(widgetPtr, value)
        }

    public var tooltipText: String?
        get() = gtk_widget_get_tooltip_text(widgetPtr)?.toKString()
        set(value) {
            gtk_widget_set_tooltip_text(widgetPtr, value)
        }

    public var visible: Boolean
        get() = Boolean from gtk_widget_get_visible(widgetPtr)
        set(value) {
            gtk_widget_set_visible(widgetPtr, value.gtkValue)
        }

    public var direction: TextDirection
        get() = TextDirection from gtk_widget_get_direction(widgetPtr)
        set(value) {
            gtk_widget_set_direction(widgetPtr, value.gtkValue)
        }

    public var parent: Widget?
        get() = gtk_widget_get_parent(widgetPtr)?.let { Widget(it) }
        set(value) {
            gtk_widget_set_parent(widgetPtr, value?.widgetPtr)
        }

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    public fun show() {
        gtk_widget_show(widgetPtr)
    }

    public fun hide() {
        gtk_widget_hide(widgetPtr)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////

    public fun onDestroy(callback: () -> Unit) {
        connectSignal(
            "destroy",
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    public fun onShow(callback: () -> Unit) {
        connectSignal(
            "show",
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    public fun onHide(callback: () -> Unit) {
        connectSignal(
            "hide",
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }
}

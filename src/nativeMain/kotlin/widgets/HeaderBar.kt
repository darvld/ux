@file:Suppress("unused")

package ux


import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString

public class HeaderBar internal constructor(
    pointer: CPointer<GtkWidget>
) : Container(pointer) {
    public constructor() : this(gtk_header_bar_new()!!)

    private val self: CPointer<GtkHeaderBar> = pointer.reinterpret()

    public var title: String?
        get() = gtk_header_bar_get_title(self)?.toKString()
        set(value) {
            gtk_header_bar_set_title(self, value)
        }

    public var subtitle: String?
        get() = gtk_header_bar_get_subtitle(self)?.toKString()
        set(value) {
            gtk_header_bar_set_subtitle(self, value)
        }

    public var hasSubtitle: Boolean
        get() = Boolean from gtk_header_bar_get_has_subtitle(self)
        set(value) {
            gtk_header_bar_set_has_subtitle(self, value.gtkValue)
        }

    public var showStandardControls: Boolean
        get() = Boolean from gtk_header_bar_get_show_close_button(self)
        set(value) {
            gtk_header_bar_set_show_close_button(self, value.gtkValue)
        }

    public var decorationLayout: String?
        get() = gtk_header_bar_get_decoration_layout(self)?.toKString()
        set(value) {
            gtk_header_bar_set_decoration_layout(self, value)
        }

    public var customTitle: Widget?
        get() = gtk_header_bar_get_custom_title(self)?.let { Widget(it) }
        set(value) {
            gtk_header_bar_set_custom_title(self, value?.widgetPtr)
        }

    public fun packStart(widget: Widget) {
        gtk_header_bar_pack_start(self, widget.widgetPtr)
    }

    public fun packEnd(widget: Widget) {
        gtk_header_bar_pack_end(self, widget.widgetPtr)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Child properties
    ///////////////////////////////////////////////////////////////////////////
    public var Widget.packType: PackType
        get() = PackType.values()[this@HeaderBar.getChildProperty(this, "pack-type").asEnumOrdinal]
        set(value) = this@HeaderBar.setChildProperty(this, "pack-type", value)

    public var Widget.positionIndex: Int
        get() = this@HeaderBar.getChildProperty(this, "position").asInt
        set(value) {
            require(value >= 0)
            this@HeaderBar.setChildProperty(this, "position", value)
        }
}

@WidgetDsl
public inline fun Window.headerBar(
    title: String? = null,
    subtitle: String? = null,
    op: HeaderBar.() -> Unit = {}
): HeaderBar {
    return HeaderBar().also { instance ->
        // Shortcuts to avoid unnecessary lambdas
        title?.let { instance.title = it }
        subtitle?.let { instance.subtitle = it }

        // Let's set this to true as a default since it's generally desirable
        instance.showStandardControls = true

        instance.show()
        instance.op()
        setTitleBar(instance)
    }
}

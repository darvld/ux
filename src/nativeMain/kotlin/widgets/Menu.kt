package ux

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import libgtk.*

public inline fun Widget.menu(op: Menu.() -> Unit = {}): Menu {
    return Menu().apply {
        op()
        attachTo(this@menu)
    }
}

public abstract class MenuShell(widgetPtr: WidgetPtr) : Container(widgetPtr) {
    private val self: CPointer<GtkMenuShell> = widgetPtr.reinterpret()

    public var takeFocus: Boolean
        get() = Boolean from gtk_menu_shell_get_take_focus(self)
        set(value) {
            gtk_menu_shell_set_take_focus(self, value.gtkValue)
        }

    public val selectedItem: Widget?
        get() = gtk_menu_shell_get_selected_item(self)?.let { Widget(it) }

    public val parentShell: Widget?
        get() = gtk_menu_shell_get_parent_shell(self)?.let { Widget(it) }

    public fun append(child: Widget) {
        gtk_menu_shell_append(self, child.widgetPtr)
    }

    public fun prepend(child: Widget) {
        gtk_menu_shell_prepend(self, child.widgetPtr)
    }

    public fun insert(child: Widget, position: Int) {
        gtk_menu_shell_insert(self, child.widgetPtr, position)
    }

    public fun deactivate() {
        gtk_menu_shell_deactivate(self)
    }

    public fun selectItem(item: Widget) {
        gtk_menu_shell_select_item(self, item.widgetPtr)
    }

    public fun selectFirst(searchSensitive: Boolean) {
        gtk_menu_shell_select_first(self, searchSensitive.gtkValue)
    }

    public fun deselect() {
        gtk_menu_shell_deselect(self)
    }

    public fun activateItem(item: Widget, forceDeactivation: Boolean) {
        gtk_menu_shell_activate_item(self, item.widgetPtr, forceDeactivation.gtkValue)
    }

    public fun cancel() {
        gtk_menu_shell_cancel(self)
    }
}

public class Menu(widgetPtr: WidgetPtr) : MenuShell(widgetPtr) {
    public constructor() : this(gtk_menu_new()!!)

    private val self: CPointer<GtkMenu> = widgetPtr.reinterpret()

    public inline fun item(label: String? = null, setup: MenuItem.() -> Unit = {}): MenuItem {
        val mItem = label?.let { MenuItem(it) } ?: MenuItem()
        mItem.apply(setup)
        append(mItem)
        mItem.show()

        return mItem
    }

    public fun reorderChild(child: Widget, position: Int) {
        gtk_menu_reorder_child(self, child.widgetPtr, position)
    }

    public fun popupAt(widget: Widget, widgetAnchor: GdkGravity, menuAnchor: GdkGravity) {
        gtk_menu_popup_at_widget(self, widget.widgetPtr, widgetAnchor, menuAnchor, null)
    }

    public fun attachTo(widget: Widget) {
        gtk_menu_attach_to_widget(self, widget.widgetPtr, null)
    }

    public fun detach() {
        gtk_menu_detach(self)
    }

}

public class MenuItem(widgetPtr: WidgetPtr) : Bin(widgetPtr) {
    public constructor() : this(gtk_menu_item_new()!!)
    public constructor(label: String) : this(gtk_menu_item_new_with_label(label)!!)

    private val self: CPointer<GtkMenuItem> = widgetPtr.reinterpret()

    public var label: String?
        get() = gtk_menu_item_get_label(self)?.toKString()
        set(value) {
            gtk_menu_item_set_label(self, value)
        }

    public var submenu: Widget?
        get() = gtk_menu_item_get_submenu(self)?.let { Widget(it) }
        set(value) {
            gtk_menu_item_set_submenu(self, value?.widgetPtr)
        }

    public fun select() {
        gtk_menu_item_select(self)
    }

    public fun deselect() {
        gtk_menu_item_deselect(self)
    }

    public fun activate() {
        gtk_menu_item_activate(self)
    }

    public fun onActivate(callback: () -> Unit) {
        connectSignal(
            "activate",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    public fun onDeselect(callback: () -> Unit) {
        connectSignal(
            "deselect",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    public fun onSelect(callback: () -> Unit) {
        connectSignal(
            "select",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }


}

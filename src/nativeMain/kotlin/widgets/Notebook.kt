package ux



import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import libgtk.*

public class Notebook(
    pointer: CPointer<GtkWidget>
) : Container(pointer) {
    public constructor() : this(gtk_notebook_new()!!)

    private val self: CPointer<GtkNotebook> = pointer.reinterpret()

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    public var currentPage: Int
        get() = gtk_notebook_get_current_page(self)
        set(value) {
            gtk_notebook_set_current_page(self, value)
        }

    public var groupName: String?
        get() = gtk_notebook_get_group_name(self)?.toKString()
        set(value) {
            gtk_notebook_set_group_name(self, value)
        }

    public val pageCount: Int
        get() = gtk_notebook_get_n_pages(self)

    public var tabPos: PositionType
        get() = PositionType from gtk_notebook_get_tab_pos(self)
        set(value) {
            gtk_notebook_set_tab_pos(self, value.gtkValue)
        }

    public var showTabs: Boolean
        get() = Boolean from gtk_notebook_get_show_tabs(self)
        set(value) {
            gtk_notebook_set_show_tabs(self, value.gtkValue)
        }

    public var showBorder: Boolean
        get() = Boolean from gtk_notebook_get_show_border(self)
        set(value) {
            gtk_notebook_set_show_border(self, value.gtkValue)
        }

    public var scrollable: Boolean
        get() = Boolean from gtk_notebook_get_scrollable(self)
        set(value) {
            gtk_notebook_set_scrollable(self, value.gtkValue)
        }

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    public fun appendPage(
        child: Widget,
        tabLabel: Widget? = null,
        menuLabel: Widget? = null
    ): Int {
        return gtk_notebook_append_page_menu(
            self,
            child.widgetPtr,
            tabLabel?.widgetPtr,
            menuLabel?.widgetPtr
        )
    }

    public fun prependPage(
        child: Widget,
        tabLabel: Widget? = null,
        menuLabel: Widget? = null
    ): Int {
        return gtk_notebook_prepend_page_menu(
            self,
            child.widgetPtr,
            tabLabel?.widgetPtr,
            menuLabel?.widgetPtr
        )
    }

    public fun insertPage(
        child: Widget,
        position: Int,
        tabLabel: Widget? = null,
        menuLabel: Widget? = null
    ): Int {
        return gtk_notebook_insert_page_menu(
            self,
            child.widgetPtr,
            tabLabel?.widgetPtr,
            menuLabel?.widgetPtr,
            position
        )
    }

    public fun removePage(position: Int) {
        gtk_notebook_remove_page(self, position)
    }

    public fun detachTab(tab: Widget) {
        gtk_notebook_detach_tab(self, tab.widgetPtr)
    }

    public fun indexOf(page: Widget): Int {
        return gtk_notebook_page_num(self, page.widgetPtr)
    }

    public fun nextPage() {
        gtk_notebook_next_page(self)
    }

    public fun prevPage() {
        gtk_notebook_prev_page(self)
    }

    public fun reorderChild(child: Widget, position: Int) {
        gtk_notebook_reorder_child(self, child.widgetPtr, position)
    }

    public fun enablePopup() {
        gtk_notebook_popup_enable(self)
    }

    public fun disablePopup() {
        gtk_notebook_popup_disable(self)
    }

    public fun getPage(index: Int): Widget? {
        return gtk_notebook_get_nth_page(self, index)?.let { Widget(it) }
    }

    public fun setMenuLabel(page: Widget, label: Widget) {
        gtk_notebook_set_menu_label(self, page.widgetPtr, label.widgetPtr)
    }

    public fun setMenuLabel(page: Widget, labelText: String) {
        gtk_notebook_set_menu_label_text(self, page.widgetPtr, labelText)
    }

    public fun getMenuLabel(page: Widget): Widget? {
        return gtk_notebook_get_menu_label(self, page.widgetPtr)?.let { Widget(it) }
    }

    public fun getMenuLabelText(page: Widget): String? {
        return gtk_notebook_get_menu_label_text(self, page.widgetPtr)?.toKString()
    }

    public fun setTabLabel(page: Widget, label: Widget) {
        gtk_notebook_set_tab_label(self, page.widgetPtr, label.widgetPtr)
    }

    public fun setTabLabel(page: Widget, labelText: String) {
        gtk_notebook_set_tab_label_text(self, page.widgetPtr, labelText)
    }

    public fun getTabLabel(page: Widget): Widget? {
        return gtk_notebook_get_tab_label(self, page.widgetPtr)?.let { Widget(it) }
    }

    public fun getTabLabelText(page: Widget): String? {
        return gtk_notebook_get_tab_label_text(self, page.widgetPtr)?.toKString()
    }

    public fun setTabReorderable(page: Widget, reorderable: Boolean) {
        gtk_notebook_set_tab_reorderable(self, page.widgetPtr, reorderable.gtkValue)
    }

    public fun setTabDetachable(page: Widget, detachable: Boolean) {
        gtk_notebook_set_tab_detachable(self, page.widgetPtr, detachable.gtkValue)
    }

    public fun getTabReorderable(page: Widget): Boolean {
        return gtk_notebook_get_tab_reorderable(self, page.widgetPtr) == 1
    }

    public fun getTabDetachable(page: Widget): Boolean {
        return gtk_notebook_get_tab_detachable(self, page.widgetPtr) == 1
    }

    public fun setActionWidget(widget: Widget, packType: PackType) {
        gtk_notebook_set_action_widget(self, widget.widgetPtr, packType.gtkValue)
    }

    public fun getActionWidget(packType: PackType): Widget? {
        return gtk_notebook_get_action_widget(self, packType.gtkValue)?.let { Widget(it) }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Extension child properties
    ///////////////////////////////////////////////////////////////////////////
    /*public var Widget.resize: Boolean
        get() = getChildProperty(this, "resize").asBoolean
        set(value) = setChildProperty(this, "resize", value)

    public var Widget.pageIndex: Int
        get() = getChildProperty(this, "position").asInt
        set(value) = setChildProperty(this, "position", value)

    public var Widget.tabExpand: Boolean
        get() = getChildProperty(this, "tab-expand").asBoolean
        set(value) = setChildProperty(this, "tab-expand", value)

    public var Widget.tabFill: Boolean
        get() = getChildProperty(this, "tab-fill").asBoolean
        set(value) = setChildProperty(this, "tab-fill", value)

    // Using old-style getters and setters
    public var Widget.tabLabel: Widget?
        get() = gtk_notebook_get_tab_label(self, widgetPtr)?.let { Widget(it) }
        set(value) {
            gtk_notebook_set_tab_label(self, widgetPtr, value?.widgetPtr)
        }

    public var Widget.tabLabelText: String?
        get() = gtk_notebook_get_tab_label_text(self, widgetPtr)?.toKString()
        set(value) {
            gtk_notebook_set_tab_label_text(self, widgetPtr, value)
        }

    public var Widget.menuLabel: Widget?
        get() = gtk_notebook_get_menu_label(self, widgetPtr)?.let { Widget(it) }
        set(value) {
            gtk_notebook_set_menu_label(self, widgetPtr, value?.widgetPtr)
        }

    public var Widget.menuLabelText: String?
        get() = gtk_notebook_get_menu_label_text(self, widgetPtr)?.toKString()
        set(value) {
            gtk_notebook_set_menu_label_text(self, widgetPtr, value)
        }

    public var Widget.tabReorderable: Boolean
        get() = Boolean from gtk_notebook_get_tab_reorderable(self, widgetPtr)
        set(value) = gtk_notebook_set_tab_reorderable(self, widgetPtr, value.gtkValue)

    public var Widget.tabDetachable: Boolean
        get() = Boolean from gtk_notebook_get_tab_detachable(self, widgetPtr)
        set(value) = gtk_notebook_set_tab_detachable(self, widgetPtr, value.gtkValue)*/
}

@WidgetDsl
public fun Container.notebook(
    op: Notebook.() -> Unit = {}
): Notebook {
    return Notebook().also { instance ->
        add(instance)
        instance.show()

        instance.op()
    }

}

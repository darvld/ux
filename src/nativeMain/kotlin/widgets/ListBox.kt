@file:Suppress("unused")

package ux

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret
import libgtk.*

public class ListBox internal constructor(pointer: WidgetPtr) : Container(pointer) {
    public constructor() : this(gtk_list_box_new()!!)

    private val self: CPointer<GtkListBox> = pointer.reinterpret()

    public var selectionMode: SelectionMode
        get() = SelectionMode from gtk_list_box_get_selection_mode(self)
        set(value) {
            gtk_list_box_set_selection_mode(self, value.gtkValue)
        }

    public var activateOnSingleClick: Boolean
        get() = Boolean from gtk_list_box_get_activate_on_single_click(self)
        set(value) {
            gtk_list_box_set_activate_on_single_click(self, value.gtkValue)
        }

    public var adjustment: Adjustment?
        get() = gtk_list_box_get_adjustment(self)?.let { Adjustment(it) }
        set(value) {
            gtk_list_box_set_adjustment(self, value?.self)
        }

    public val selectedRow: Row?
        get() = gtk_list_box_get_selected_row(self)?.let { Row(it.reinterpret()) }

    public val selectedRows: List<Row>
        get() {
            val cList = gtk_list_box_get_selected_rows(self) ?: return emptyList()
            return cList.asSequence<GtkListBoxRow>().map { Row(it.reinterpret()) }.toList()
        }

    public fun prepend(child: Widget) {
        gtk_list_box_prepend(self, child.widgetPtr)
    }

    public fun insert(child: Widget, position: Int) {
        gtk_list_box_insert(self, child.widgetPtr, position)
    }

    public fun selectRow(row: Row) {
        gtk_list_box_select_row(self, row.self)
    }

    public fun unselectRow(row: Row) {
        gtk_list_box_unselect_row(self, row.self)
    }

    public fun selectAll() {
        gtk_list_box_select_all(self)
    }

    public fun unselectAll() {
        gtk_list_box_unselect_all(self)
    }

    public fun setPlaceholder(placeholder: Widget?) {
        gtk_list_box_set_placeholder(self, placeholder?.widgetPtr)
    }

    public fun getRowAt(index: Int): Row? {
        return gtk_list_box_get_row_at_index(self, index)?.let { Row(it.reinterpret()) }
    }

    public fun getRowAtY(y: Int) {
        gtk_list_box_get_row_at_y(self, y)
    }

    public fun invalidateFilter() {
        gtk_list_box_invalidate_filter(self)
    }

    public fun invalidateHeaders() {
        gtk_list_box_invalidate_headers(self)
    }

    public fun invalidateSort() {
        gtk_list_box_invalidate_sort(self)
    }

    public fun row(
        activatable: Boolean = true,
        selectable: Boolean = true,
        setup: Row.() -> Unit = {}
    ): Row {
        return Row().also {
            add(it)

            it.show()
            
            it.activatable = activatable
            it.selectable = selectable

            it.setup()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////
    public fun onRowActivated(callback: (Row) -> Unit) {
        connectSignal(
            "row-activated",
            staticCallback1,
            StableRef.create { ptr: CPointer<GtkWidget> ->
                callback(Row(ptr))
            }.asCPointer()
        )
    }

    public class Row(pointer: WidgetPtr) : Bin(pointer) {
        internal constructor() : this(gtk_list_box_row_new()!!)

        internal val self: CPointer<GtkListBoxRow> = pointer.reinterpret()

        public val index: Int
            get() = gtk_list_box_row_get_index(self)

        public var activatable: Boolean
            get() = Boolean from gtk_list_box_row_get_activatable(self)
            set(value) {
                gtk_list_box_row_set_activatable(self, value.gtkValue)
            }

        public var selectable: Boolean
            get() = Boolean from gtk_list_box_row_get_selectable(self)
            set(value) {
                gtk_list_box_row_set_selectable(self, value.gtkValue)
            }

        public val isSelected: Boolean
            get() = Boolean from gtk_list_box_row_is_selected(self)

        public var header: Widget?
            get() = gtk_list_box_row_get_header(self)?.let { Widget(it) }
            set(value) {
                gtk_list_box_row_set_header(self, value?.widgetPtr)
            }

        public fun changed() {
            gtk_list_box_row_changed(self)
        }
    }
}

@WidgetDsl
public fun Container.listBox(
    op: ListBox.() -> Unit = {},
): ListBox {
    return ListBox().also { instance ->
        add(instance)
        instance.show()

        instance.op()
    }
}

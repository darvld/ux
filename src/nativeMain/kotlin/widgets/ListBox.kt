@file:Suppress("unused")

package ux

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret
import libgtk.*

public typealias ListBoxRow = CPointer<GtkListBoxRow>

public class ListBox internal constructor(pointer: CPointer<GtkWidget>) : Container(pointer) {
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

    public val selectedRow: ListBoxRow?
        get() = gtk_list_box_get_selected_row(self)

    public val selectedRows: List<ListBoxRow>
        get() {
            val cList = gtk_list_box_get_selected_rows(self) ?: return emptyList()
            return cList.asSequence<GtkListBoxRow>().toList()
        }

    public fun prepend(child: Widget) {
        gtk_list_box_prepend(self, child.widgetPtr)
    }

    public fun insert(child: Widget, position: Int) {
        gtk_list_box_insert(self, child.widgetPtr, position)
    }

    public fun selectRow(row: ListBoxRow) {
        gtk_list_box_select_row(self, row)
    }

    public fun unselectRow(row: ListBoxRow) {
        gtk_list_box_unselect_row(self, row)
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

    public fun getRowAt(index: Int): ListBoxRow? {
        return gtk_list_box_get_row_at_index(self, index)
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

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////
    public fun onRowActivated(callback: (ListBoxRow) -> Unit) {
        connectSignal(
            "row-activated",
            staticCallback1,
            StableRef.create(callback).asCPointer()
        )
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

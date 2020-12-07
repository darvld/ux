@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ux




import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

public class TreeView(
    pointer: CPointer<GtkWidget>,
) : Container(pointer) {
    public constructor() : this(gtk_tree_view_new()!!)
    public constructor(model: TreeModel) : this(gtk_tree_view_new()!!){
        this.model = model
    }

    internal val self: CPointer<GtkTreeView> = pointer.reinterpret()

    public var model: TreeModel? = null
        set(value) {
            gtk_tree_view_set_model(self, value?.treePointer)
            field = value
        }

    public var showExpanders: Boolean
        get() = Boolean from gtk_tree_view_get_show_expanders(self)
        set(value) {
            gtk_tree_view_set_show_expanders(self, value.gtkValue)
        }

    public var levelIndentation: Int
        get() = gtk_tree_view_get_level_indentation(self)
        set(value) {
            gtk_tree_view_set_level_indentation(self, value)
        }

    public class Selection internal constructor(internal val self: CPointer<GtkTreeSelection>) :
        GObjectWrapper(self.reinterpret()) {

        public var mode: SelectionMode
            get() = SelectionMode from gtk_tree_selection_get_mode(self)
            set(value) {
                gtk_tree_selection_set_mode(self, value.gtkValue)
            }
    }

    public fun TreeViewColumn.bind(property: String, modelColumn: String) {
        gtk_tree_view_column_add_attribute(
            self,
            renderer.cellPointer,
            property,
            model!!.getColumnIndex(modelColumn)
        )
    }

    public fun addColumn(title: String, renderer: CellRenderer, setup: TreeViewColumn.() -> Unit): TreeViewColumn {
        return TreeViewColumn(title, renderer).apply(setup).also {
            gtk_tree_view_append_column(self, it.self)
        }
    }

    public fun removeColumn(column: TreeViewColumn) {
        gtk_tree_view_remove_column(self, column.self)
    }
}

@WidgetDsl
public fun Container.treeView(
    model: TreeModel? = null,
    op: TreeView.() -> Unit = {}
): TreeView {
    val view = model?.let { TreeView(it) } ?: TreeView()

    add(view)
    view.show()

    view.op()
    return view
}

@file:Suppress("unused")

package ux


import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

/**Represents a visual column in a [TreeView].*/
public class TreeViewColumn internal constructor(
    /**The name of this column, as displayed by the [TreeView].*/
    public val name: String,
    /**The cell renderer responsible for drawing this column's cells.*/
    internal val renderer: CellRenderer,
) {
    internal val self: CPointer<GtkTreeViewColumn> =
        gtk_tree_view_column_new_with_attributes(name, renderer.cellPointer, null)!!

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    public var spacing: Int
        get() = gtk_tree_view_column_get_spacing(self)
        set(value) {
            gtk_tree_view_column_set_spacing(self, value)
        }

    public var visible: Boolean
        get() = Boolean from gtk_tree_view_column_get_visible(self)
        set(value) {
            gtk_tree_view_column_set_visible(self, value.gtkValue)
        }

    public var resizable: Boolean
        get() = Boolean from gtk_tree_view_column_get_resizable(self)
        set(value) {
            gtk_tree_view_column_set_resizable(self, value.gtkValue)
        }

    public var sizing: Sizing
        get() = Sizing from gtk_tree_view_column_get_sizing(self)
        set(value) {
            gtk_tree_view_column_set_sizing(self, value.gtkValue)
        }

    public val width: Int
        get() = gtk_tree_view_column_get_width(self)

    public var fixedWidth: Int
        get() = gtk_tree_view_column_get_fixed_width(self)
        set(value) {
            gtk_tree_view_column_set_fixed_width(self, value)
        }

    public var minWidth: Int
        get() = gtk_tree_view_column_get_min_width(self)
        set(value) {
            gtk_tree_view_column_set_min_width(self, value)
        }

    public var maxWidth: Int
        get() = gtk_tree_view_column_get_max_width(self)
        set(value) {
            gtk_tree_view_column_set_max_width(self, value)
        }

    public var title: String
        get() = gtk_tree_view_column_get_title(self)?.toKString() ?: ""
        set(value) {
            gtk_tree_view_column_set_title(self, value)
        }

    public var expand: Boolean
        get() = Boolean from gtk_tree_view_column_get_expand(self)
        set(value) {
            gtk_tree_view_column_set_expand(self, value.gtkValue)
        }

    public var clickable: Boolean
        get() = Boolean from gtk_tree_view_column_get_clickable(self)
        set(value) {
            gtk_tree_view_column_set_clickable(self, value.gtkValue)
        }

    public var headerWidget: Widget?
        get() = gtk_tree_view_column_get_widget(self)?.let { Widget(it) }
        set(value) {
            gtk_tree_view_column_set_widget(self, value?.widgetPtr)
        }

    public val headerButton: Widget?
        get() = gtk_tree_view_column_get_button(self)?.let { Widget(it) }

    public var alignment: Float
        get() = gtk_tree_view_column_get_alignment(self)
        set(value) {
            gtk_tree_view_column_set_alignment(self, value)
        }

    public var reorderable: Boolean
        get() = Boolean from gtk_tree_view_column_get_reorderable(self)
        set(value) {
            gtk_tree_view_column_set_reorderable(self, value.gtkValue)
        }

    public var sortColumnId: Int
        get() = gtk_tree_view_column_get_sort_column_id(self)
        set(value) {
            gtk_tree_view_column_set_sort_column_id(self, value)
        }

    public var sortIndicator: Boolean
        get() = Boolean from gtk_tree_view_column_get_sort_indicator(self)
        set(value) {
            gtk_tree_view_column_set_sort_indicator(self, value.gtkValue)
        }


    public enum class Sizing {
        GrowOnly,
        AutoSize,
        Fixed;

        internal val gtkValue = GtkTreeViewColumnSizing.values()[ordinal]

        internal companion object {
            internal infix fun from(gtkValue: GtkTreeViewColumnSizing) = values()[gtkValue.ordinal]
        }
    }
}

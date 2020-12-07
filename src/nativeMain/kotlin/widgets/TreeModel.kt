@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ux





import libgtk.*
import kotlinx.cinterop.*

public typealias ModelColumn<T> = Pair<GType, (T) -> Any?>

public open class TreeModel internal constructor(internal val treePointer: CPointer<GtkTreeModel>) :
    GObjectWrapper(treePointer.reinterpret()) {

    public object Flags {
        public const val IteratorsPersist: Int = 1 shl 0
        public const val ListOnly: Int = 1 shl 1
    }

    public open fun getColumnIndex(name: String): Int = -1

    public inner class TreeRowReference internal constructor(
        internal val self: CPointer<GtkTreeRowReference>
    ) {
        public constructor(path: TreePath) : this(
            gtk_tree_row_reference_new(
                treePointer,
                path.self
            )!!
        )

        public fun free() {
            gtk_tree_row_reference_free(self)
        }

        public fun copy(): TreeRowReference {
            return TreeRowReference(gtk_tree_row_reference_copy(self)!!)
        }

        public val model: TreeModel = this@TreeModel
        public val path: TreePath?
            get() = gtk_tree_row_reference_get_path(self)?.let { TreePath(it) }
    }

    public inner class TreeIterator(internal val self: CPointer<GtkTreeIter>) {

        public val hasChildren: Boolean
            get() = gtk_tree_model_iter_has_child(treePointer, self) == 1

        public val childCount: Int
            get() = gtk_tree_model_iter_n_children(treePointer, self)

        public fun getChildAt(index: Int = 0): TreeIterator? {
            val iter = nativeHeap.alloc<GtkTreeIter>().ptr
            if (gtk_tree_model_iter_nth_child(treePointer, iter, self, index) == 1) return null

            return TreeIterator(iter)
        }

        public val parent: TreeIterator?
            get() {
                val iter = nativeHeap.alloc<GtkTreeIter>().ptr
                if (gtk_tree_model_iter_parent(treePointer, iter, self) == 1) return null

                return TreeIterator(iter)
            }

        public val path: TreePath
            get() = TreePath(gtk_tree_model_get_path(treePointer, self)!!)

        public fun next(): Boolean {
            return Boolean from gtk_tree_model_iter_next(treePointer, self)
        }

        public fun previous(): Boolean {
            return Boolean from gtk_tree_model_iter_previous(treePointer, self)
        }

        public fun children(): TreeIterator? {
            val iter = nativeHeap.alloc<GtkTreeIter>().ptr
            if (gtk_tree_model_iter_children(treePointer, iter, self) == 1) return null

            return TreeIterator(iter)
        }

        public fun ref() {
            gtk_tree_model_ref_node(treePointer, self)
        }

        public fun unref() {
            gtk_tree_model_unref_node(treePointer, self)
        }

        @Suppress("UNCHECKED_CAST")
        public fun <T> getValue(column: Int): T? {
            val v = nativeHeap.alloc<GValue>().ptr
            gtk_tree_model_get_value(treePointer, self, column, v)
            return GValueWrapper(v).getValue<Any?>() as? T
        }

        override fun toString(): String {
            return gtk_tree_model_get_string_from_iter(treePointer, self)?.toKString() ?: "null"
        }
    }

    public val flags: Int
        get() = gtk_tree_model_get_flags(treePointer).convert()

    public val columnAmount: Int
        get() = gtk_tree_model_get_n_columns(treePointer)

    public fun getColumnType(index: Int): GType {
        return gtk_tree_model_get_column_type(treePointer, index)
    }

    public fun getIterator(path: TreePath): TreeIterator? {
        val iter = nativeHeap.alloc<GtkTreeIter>().ptr
        if (gtk_tree_model_get_iter(treePointer, iter, path.self) == 0) return null

        return TreeIterator(iter)
    }

    public fun getIterator(path: String): TreeIterator? {
        val iter = nativeHeap.alloc<GtkTreeIter>().ptr
        if (gtk_tree_model_get_iter_from_string(treePointer, iter, path) == 0) return null

        return TreeIterator(iter)
    }

    public fun getFirstIterator(): TreeIterator? {
        val iter = nativeHeap.alloc<GtkTreeIter>().ptr
        if (gtk_tree_model_get_iter_first(treePointer, iter) == 0) return null

        return TreeIterator(iter)
    }

}

public val TreeModel.TreeRowReference?.valid: Boolean get() = this != null && gtk_tree_row_reference_valid(self) == 1

/**Base class for all TreeModel-derived classes like [ListStore] and [TreeStore].*/
public abstract class TreeModelBuilderScope<T> {
    protected val columns: LinkedHashMap<String, ModelColumn<T>> = LinkedHashMap()

    public fun column(name: String, affinity: GlibType = GlibType.String, mapFunction: (T) -> Any?) {
        columns[name] = affinity.gtkValue to mapFunction
    }

    internal abstract fun build(): TreeModel
}

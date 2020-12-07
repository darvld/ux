@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ux


import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.toKString

public class TreePath internal constructor(internal val self: CPointer<GtkTreePath>) {
    public constructor() : this(gtk_tree_path_new_first()!!)
    public constructor(path: String) : this(gtk_tree_path_new_from_string(path)!!)

    public val depth: Int
        get() = gtk_tree_path_get_depth(self)

    public val indices: IntArray?
        get() {
            val array = gtk_tree_path_get_indices(self) ?: return null
            return IntArray(depth) { array[it] }
        }

    public fun copy(): TreePath {
        return TreePath(gtk_tree_path_copy(self)!!)
    }

    public fun compare(other: TreePath): Int {
        return gtk_tree_path_compare(self, other.self)
    }

    public fun next() {
        gtk_tree_path_next(self)
    }

    public fun prev() {
        gtk_tree_path_prev(self)
    }

    public fun up() {
        gtk_tree_path_up(self)
    }

    public fun down() {
        gtk_tree_path_down(self)
    }

    public infix fun ancestorOf(of: TreePath): Boolean {
        return Boolean from gtk_tree_path_is_ancestor(self, of.self)
    }

    public infix fun descendantOf(of: TreePath): Boolean {
        return Boolean from gtk_tree_path_is_descendant(self, of.self)
    }

    public fun free() {
        gtk_tree_path_free(self)
    }

    public fun appendIndex(index: Int) {
        gtk_tree_path_append_index(self, index)
    }

    public fun prependIndex(index: Int) {
        gtk_tree_path_prepend_index(self, index)
    }

    override fun toString(): String {
        return gtk_tree_path_to_string(self)?.toKString() ?: "null"
    }
}

@file:Suppress("unused")

package ux



import kotlinx.cinterop.*
import libgtk.*

public class ListStoreBuilderScope<T> : TreeModelBuilderScope<T>() {
    override fun build(): ListStore<T> = memScoped {
        val listPointer = allocArray<GTypeVar>(columns.size)

        for ((i, value) in columns.values.withIndex()) {
            listPointer[i] = value.first
        }

        return ListStore(
            gtk_list_store_newv(columns.size, listPointer)!!,
            columns
        )
    }
}

public class ListStore<T> internal constructor(
    internal val self: CPointer<GtkListStore>,
    private val columns: LinkedHashMap<String, ModelColumn<T>>
) : TreeModel(self.reinterpret()) {

    // This is purely for convenience, to avoid unnecessary calls to columns.size
    private val columnCount = columns.size

    private inline fun insertValues(
        vararg items: T,
        iteratorInit: (CPointer<GtkTreeIter>) -> Unit
    ) = memScoped {
        // Create the iterator and set it to the append position
        val iter = alloc<GtkTreeIter>().ptr
        val columnList = allocArray<IntVar>(columnCount) { value = it }
        val argList = allocArray<GValue>(columnCount)

        for (item in items) {
            // Get the append position
            iteratorInit(iter)

            // Fill in the values for each model column in this row
            for ((index, column) in columns.values.withIndex()) {
                val pointer = argList[index].ptr
                g_value_unset(pointer)

                GValueWrapper(pointer, value = column.second(item))
            }

            // Set the values
            gtk_list_store_set_valuesv(self, iter, columnList, argList, columnCount)
        }
    }

    public fun prepend(vararg items: T) {
        insertValues(*items) { gtk_list_store_prepend(self, it) }
    }

    public fun append(vararg items: T) {
        insertValues(*items) { gtk_list_store_append(self, it) }
    }

    public fun insert(position: Int, vararg items: T) {
        insertValues(*items) { gtk_list_store_insert(self, it, position) }
    }

    public fun getValues(position: Int): Map<String, Any?>? {
        val iter = getIterator(position.toString()) ?: return null

        return columns.keys.withIndex().associate { indexedColumn  ->
            indexedColumn.value to iter.getValue(indexedColumn.index)
        }
    }

    public fun remove(position: Int) {
        getIterator(position.toString())?.let { gtk_list_store_remove(self, it.self) }
    }

    public fun clear() {
        gtk_list_store_clear(self)
    }

    override fun getColumnIndex(name: String): Int = columns.keys.indexOfFirst { it == name }
}

@WidgetDsl
/**Creates a new [ListStore].
 *
 * You can use the provided [scope][ListStoreBuilderScope] to add columns to this [ListStore] by calling the
 * [column][TreeModelBuilderScope.column] method.*/
public fun <T> listStore(setup: ListStoreBuilderScope<T>.() -> Unit): ListStore<T> {
    return ListStoreBuilderScope<T>().apply(setup).build()
}


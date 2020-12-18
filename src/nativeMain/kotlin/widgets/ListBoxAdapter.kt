@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ux

public class AdapterList<T> {
    public val items: MutableList<ViewHolder<T>> = mutableListOf()

    public inline operator fun get(index: Int): T {
        return items[index].item
    }

    public inline operator fun set(index: Int, value: T) {
        items[index].bind(value)
    }

    public inline fun modify(index: Int, op: T.() -> Unit) {
        items[index].apply {
            bind(item.apply(op))
        }
    }

    public inline operator fun iterator(): MutableIterator<ViewHolder<T>> = items.iterator()
}

public abstract class ListBoxAdapter<T>(protected val listBox: ListBox) {
    public val rows: AdapterList<T> = AdapterList()

    protected abstract fun createRow(): ViewHolder<T>

    public fun add(item: T) {
        createRow().also {
            it.init(listBox)
            it.bind(item)
            rows.items.add(it)
        }
    }

    public fun remove(row: ViewHolder<T>) {
        rows.items.remove(row)
        row.destroy()
    }

    public fun remove(item: T) {
        val index = rows.items.indexOfFirst { it.item == item }
        remove(index)
    }

    public fun remove(index: Int) {
        rows.items[index].destroy()
        rows.items.removeAt(index)
    }

    public fun clear() {
        for (row in rows) {
            remove(row)
        }
    }
}

public abstract class ViewHolder<T> {
    protected lateinit var view: Widget
    internal fun init(container: ListBox) {
        view = container.createView()
    }

    internal fun destroy() {
        onDestroy()
        view.destroy()
    }

    public abstract val item: T
    public abstract fun bind(item: T)

    public abstract fun ListBox.createView(): Widget
    public open fun onDestroy() {}
}

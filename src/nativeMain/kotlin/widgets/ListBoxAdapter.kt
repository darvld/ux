@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ux

public abstract class ListBoxAdapter<T>(protected val listBox: ListBox) {
    protected val items: MutableMap<T, ViewHolder<T>> = mutableMapOf()

    protected abstract fun createRow(): ViewHolder<T>

    public fun add(item: T) {
        items.getOrPut(item) {
            createRow().also {
                it.init(listBox)
                it.bind(item)
            }
        }
    }

    public fun addAll(vararg items: T) {
        for (item in items) add(item)
    }

    public fun remove(item: T) {
        items.remove(item)?.destroy()
    }

    public fun removeAll(vararg items: T) {
        for (item in items) remove(item)
    }

    public fun modify(item: T, op: T.() -> Unit) {
        val holder = items[item] ?: throw Exception("Modified item must be added to this adapter first")
        item.apply(op)
        holder.bind(item)
    }

    public fun modify(index: Int, op: T.() -> Unit) {
        for ((i, key) in items.keys.withIndex()) {
            if (i != index)
                continue

            key.op()
            items[key]!!.bind(key)
            break
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

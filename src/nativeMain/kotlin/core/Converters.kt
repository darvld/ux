@file:Suppress("unused")

package ux

import libgtk.*
import kotlinx.cinterop.*

internal typealias Void = COpaquePointer

internal inline val Boolean.gtkValue: gboolean get() = if (this) 1 else 0
internal inline infix fun Boolean.Companion.from(gtkValue: gboolean): Boolean = gtkValue == 1


/**Shorthand for C's representation of a string.*/
internal typealias CString = CPointer<ByteVar>
/**Shorthand for C's representation of a list of strings (a pointer to a list of char pointers).*/
internal typealias CStringList = CPointer<CPointerVar<ByteVar>>

internal fun CStringList?.toStringList(length: Int): List<String?> {
    this ?: return emptyList()
    return List(length) { index ->
        get(index)?.toKString()
    }
}

internal fun CStringList?.toStringListNoNulls(length: Int): List<String> {
    this ?: return emptyList()
    return List(length) { index ->
        get(index)!!.toKString()
    }
}

internal fun CStringList?.toStringListFillNulls(length: Int): List<String> {
    this ?: return emptyList()
    return List(length) { index ->
        get(index)?.toKString() ?: "null"
    }
}

internal fun CStringList?.toStringListFilterNulls(length: Int): List<String> {
    this ?: return emptyList()
    return List(length) { index ->
        get(index)?.toKString()
    }.filterNotNull()
}

internal fun CPointer<GSList>?.free() {
    g_slist_free(this)
}

internal fun <T : CPointed> CPointer<GSList>?.asSequence(): Sequence<CPointer<T>> {
    val length = g_slist_length(this).toInt()
    return sequence {
        repeat(length) { index ->
            val element = g_slist_nth_data(this@asSequence, index.convert())
            yield(element!!.reinterpret<T>().also { g_free(it) })
        }
        free()
    }
}

internal fun <T : CPointed> CPointer<GList>?.asSequence(): Sequence<CPointer<T>> {
    val length = g_list_length(this).toInt()
    return sequence {
        repeat(length) { index ->
            val element = g_list_nth_data(this@asSequence, index.convert())
            yield(element!!.reinterpret<T>())
        }
    }
}

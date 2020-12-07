package ux



import libgtk.*
import kotlinx.cinterop.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


internal fun <T> T.gType(hint: GType? = null): GType = hint ?: when (this) {
    is String? -> G_TYPE_STRING
    is Boolean? -> G_TYPE_BOOLEAN
    is Int? -> G_TYPE_INT
    is Long? -> G_TYPE_LONG
    is Double? -> G_TYPE_DOUBLE
    is Float? -> G_TYPE_FLOAT
    is Char? -> G_TYPE_CHAR
    is Enum<*>? -> G_TYPE_ENUM
    else -> {
        requireNotNull(hint) { "Cannot map $this to GValue, please provide a valid type hint" }
    }
}

internal fun <T> MemScope.gvalue(value: T? = null, typeHint: GType? = null /*Auto*/): GValueWrapper {
    val ptr = alloc<GValue>().ptr

    g_value_init(ptr, value.gType(typeHint))

    return GValueWrapper(ptr).apply {
        setValue(value)
    }
}

/**Convenience class used to manage a [GValue].*/
internal class GValueWrapper(val pointer: CPointer<GValue>) {
    internal constructor(pointer: CPointer<GValue>, value: Any?, typeHint: GType? = null) : this(pointer) {
        init(value.gType(typeHint))
        setValue(value)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun init(type: GType) {
        g_value_init(pointer, type)
    }

    fun <T> setValue(value: T? = null) {
        when (value) {
            is String? -> {
                g_value_set_string(pointer, value as String?)
            }
            is Boolean? -> {
                g_value_set_boolean(pointer, (value as Boolean).gtkValue)
            }
            is Int? -> {
                g_value_set_int(pointer, value as Int)
            }
            is Long? -> {
                g_value_set_long(pointer, (value as Long).convert())
            }
            is Double? -> {
                g_value_set_double(pointer, value as Double)
            }
            is Float? -> {
                g_value_set_float(pointer, value as Float)
            }
            is Char? -> {
                g_value_set_char(pointer, (value as Char).toByte())
            }
            is Enum<*>? -> {
                g_value_set_enum(pointer, (value as Enum<*>).ordinal)
            }
            else -> {
                throw Exception("Unrecognized type")
            }
        }
    }

    inline fun <reified T> getValue(): T {
        return when (T::class.simpleName) {
            "String" -> asString as T
            "Boolean" -> asBoolean as T
            "Int" -> asInt as T
            "Long" -> asLong as T
            "Double" -> asDouble as T
            "Float" -> asFloat as T
            "Char" -> asChar as T
            else -> {
                this as T
            }
        }
    }

    inline val asString: String? get() = g_value_get_string(pointer)?.toKString()
    inline val asBoolean: Boolean get() = Boolean from g_value_get_boolean(pointer)
    inline val asInt: Int get() = g_value_get_int(pointer)
    inline val asLong: Long get() = g_value_get_long(pointer).toLong()
    inline val asDouble: Double get() = g_value_get_double(pointer)
    inline val asFloat: Float get() = g_value_get_float(pointer)
    inline val asChar: Char get() = g_value_get_char(pointer).toChar()
    inline val asEnumOrdinal: Int get() = g_value_get_enum(pointer)
}

/**Convenience class to manage a [GObject]'s properties ([get][getProperty]/[set][setProperty]).*/
public open class GObjectWrapper internal constructor(internal val objectPointer: CPointer<GObject>) {

    public fun free() {
        g_free(objectPointer)
    }

    /**Returns the current value stored in the property identified by [name].
     *
     * You can use the returned [GValueWrapper] to obtain the actual value.*/
    internal inline fun getProperty(name: String): GValueWrapper = memScoped {
        val wrapper = alloc<GValue>().ptr
        g_object_get_property(objectPointer, name, wrapper)

        return GValueWrapper(wrapper)
    }

    /**Sets the value of a property identified by [name]. The [GType] of the value is automatically determined based
     *  on the generic [T] parameter. If it's not possible to automatically infer the [GType], a valid [typeHint] must
     *  be provided.*/
    internal inline fun <T> setProperty(name: String, value: T? = null, typeHint: GType? = null) = memScoped {
        val wrapper = gvalue(value, typeHint)
        g_object_set_property(objectPointer, name, wrapper.pointer)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is GObjectWrapper) return false
        return objectPointer == other.objectPointer
    }

    override fun hashCode(): Int {
        return objectPointer.hashCode()
    }
}

internal inline fun <reified T> GObjectWrapper.gProperty(
    name: String? = null,
    noinline valueRestriction: ((T) -> Boolean)? = null,
    noinline converter: (GValueWrapper) -> T = { it.getValue() },
): GPropertyDelegate<T> = GPropertyDelegate(this, name, valueRestriction, converter)

/**A property delegate for classes that contain a [GObjectWrapper] reference.
 *
 * ```
 * val name: String by gProperty()
 * ```*/
internal class GPropertyDelegate<T>(
    private val obj: GObjectWrapper,
    private val name: String? = null,
    private val valueRestriction: ((T) -> Boolean)? = null,
    private val converter: (GValueWrapper) -> T
) : ReadWriteProperty<Any, T> {

    private lateinit var propName: String


    private inline fun getPropName(prop: KProperty<*>) {
        if (!::propName.isInitialized)
            propName = name ?: prop.name
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        getPropName(property)
        valueRestriction?.let { require(it(value)) }

        obj.setProperty(propName, value)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        getPropName(property)
        return converter(obj.getProperty(propName))
    }
}

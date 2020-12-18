package core

public class Bundle(
    /**The values contained in this bundle. You should not modify this directly.*/
    public val values: MutableMap<String, Any?>
) {
    public constructor() : this(mutableMapOf())
    public constructor(vararg values: Pair<String, Any?>) : this(mutableMapOf(*values))
    public constructor(init: Bundle.() -> Unit) : this() { init() }


    public inline fun <reified T : Any?> get(key: String, default: T?): T? {
        return (values[key] as? T) ?: default
    }

    public inline fun <reified T : Any?> put(key: String, value: T?) {
        values[key] = value
    }

    override fun toString(): String {
        return values.toList().joinToString {
            "${it.first}: ${it.second}"
        }
    }
}

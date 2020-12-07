@file:Suppress("MemberVisibilityCanBePrivate", "unused", "unused")

package ux


import libgtk.GdkRGBA
import libgtk.gdk_rgba_equal
import libgtk.gdk_rgba_hash
import libgtk.gdk_rgba_parse
import kotlinx.cinterop.*

public class Color internal constructor(internal val self: CPointer<GdkRGBA>) {
    private val rgba: GdkRGBA = self.pointed

    public var red: Double
        get() = rgba.red
        set(value) {
            rgba.red = value
        }

    public var green: Double
        get() = rgba.green
        set(value) {
            rgba.green = value
        }

    public var blue: Double
        get() = rgba.blue
        set(value) {
            rgba.blue = value
        }

    public var alpha: Double
        get() = rgba.alpha
        set(value) {
            rgba.alpha = value
        }


    override fun equals(other: Any?): Boolean {
        if (other !is Color) return false

        return Boolean from gdk_rgba_equal(self, other.self)
    }

    override fun hashCode(): Int {
        return gdk_rgba_hash(self).toInt()
    }

    override fun toString(): String {
        return "Color(red=$red, green=$green, blue=$blue, alpha=$alpha)"
    }

    public companion object {
        public operator fun invoke(r: Double, g: Double, b: Double, a: Double = 1.0): Color {
            val color = nativeHeap.alloc<GdkRGBA>().apply {
                red = r; green = g; blue = b; alpha = a
            }

            return Color(color.ptr)
        }

        public fun parse(string: String): Color? {
            val color = nativeHeap.alloc<GdkRGBA>().ptr

            return if (gdk_rgba_parse(color, string) == 1) Color(color) else null
        }
    }
}

package ux


import kotlinx.cinterop.MemScope
import kotlinx.cinterop.alloc
import libgtk.GdkRectangle


public data class Rectangle(
    public val width: Int,
    public val height: Int,
    public val x: Int,
    public val y: Int
) {
    public constructor(size: Vector, position: Vector) : this(size.x, size.y, position.x, position.y)

    internal companion object {
        internal infix fun from(rect: GdkRectangle): Rectangle {
            return Rectangle(rect.width, rect.height, rect.x, rect.y)
        }
    }

    internal fun toGdkRect(memScope: MemScope): GdkRectangle = memScope.alloc {
        width = this.width
        height = this.height
        x = this.x
        y = this.y
    }

}

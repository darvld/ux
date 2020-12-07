package ux

public data class Vector(var x: Int, var y: Int) {
    public operator fun plus(other: Vector) {
        x += other.x
        y += other.y
    }

    public operator fun plusAssign(other: Vector) {
        plus(other)
    }

    public operator fun times(scalar: Int) {
        x *= scalar
        y *= scalar
    }

    public operator fun timesAssign(scalar: Int) {
        times(scalar)
    }

    public operator fun div(scalar: Int) {
        x /= scalar
        y /= scalar
    }

    public operator fun divAssign(scalar: Int) {
        div(scalar)
    }
}

public inline infix fun Int.by(other: Int): Vector = Vector(this,other)

package ux

public data class Packing(
    public var type: PackType,
    public var expand: Boolean = false,
    public var fill: Boolean = false,
    public var padding: Int = 0,
)

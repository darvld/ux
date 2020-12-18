@file:Suppress("unused")

package ux





import libgtk.*


import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret

public class ShortcutsWindow(
    pointer: WidgetPtr
) : Window(pointer) {
    internal val self: CPointer<GtkShortcutsWindow> = pointer.reinterpret()

    public var currentSection: String by obj.gProperty("section-name")
    public var viewNameFilter: String? by obj.gProperty("view-name")

    public fun onClose(callback: () -> Unit) {
        connectSignal(
            "close",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    public fun onSearch(callback: () -> Unit) {
        connectSignal(
            "search",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }
}

public class ShortcutsSection(
    pointer: WidgetPtr
) : Box(pointer) {
    private val self: CPointer<GtkShortcutsSection> = pointer.reinterpret()

    public var maxHeight: Int by obj.gProperty("max-height")
    public var sectionName: String by obj.gProperty("section-name")
    public var title: String by obj.gProperty("title")
    public val viewName: String by obj.gProperty("view-name")
}

public class ShortcutsGroup(
    pointer: WidgetPtr
) : Box(pointer) {
    private val self: CPointer<GtkShortcutsGroup> = pointer.reinterpret()

    public var title: String by obj.gProperty("title")
    public val view: String? by obj.gProperty("view")
}

public class Shortcut(
    pointer: WidgetPtr
) : Box(pointer) {
    private val self: CPointer<GtkShortcutsShortcut> = pointer.reinterpret()


}

@file:Suppress("unused")

package ux




import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import libgtk.GtkTextTag
import libgtk.gtk_text_tag_get_priority
import libgtk.gtk_text_tag_new
import libgtk.gtk_text_tag_set_priority

/**A tag is an attribute that can be applied to some range of text. Tags are represented by [TextTag] objects.
 *
 * One [TextTag] can be applied to any number of text ranges in any number of buffers.
 *
 * For example, a tag might be called "bold" and make the text inside the tag bold. However, the tag concept is more
 * general than that; tags don't have to affect appearance. They can instead affect the behavior of mouse and key
 * presses, "lock" a range of text so the user can't edit it, or countless other things.*/
public open class TextTag internal constructor(
    internal val self: CPointer<GtkTextTag>
) {
    private val obj: GObjectWrapper = GObjectWrapper(self.reinterpret())

    public constructor(name: String? = null) : this(gtk_text_tag_new(name)!!)

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////

    /**The priority of a [TextTag]. Valid priorities start at 0 and go to one less than [TextTagTable.size].
     *
     * Each tag in a table has a unique priority; setting the priority of one tag shifts the priorities of all the
     * other tags in the table to maintain a unique priority for each tag. Higher priority tags “win” if two tags both
     * set the same text attribute.
     *
     * When adding a tag to a tag table, it will be assigned the highest priority in the table by default; so normally
     * the precedence of a set of tags is the order in which they were added to the table, or created with
     * [TextBuffer.createTag], which adds the tag to the buffer’s table automatically.*/
    public var priority: Int
        get() = gtk_text_tag_get_priority(self)
        set(value) {
            gtk_text_tag_set_priority(self, value)
        }

    public val name: String by obj.gProperty()
    public var backgroundFullHeight: Boolean by obj.gProperty("background-full-height")

    public var direction: TextDirection by obj.gProperty("direction") {
        TextDirection.values()[it.asEnumOrdinal]
    }

    public var editable: Boolean by obj.gProperty()
    public var fallback: Boolean by obj.gProperty("fallback")
    public var family: String by obj.gProperty("family")
    public var font: String by obj.gProperty("font")

    public var foregroundRGBA: String by obj.gProperty("foreground-rgba")

    public var indent: Int by obj.gProperty("indent")
    public var leftMargin: Int by obj.gProperty("left-margin", valueRestriction = { it >= 0 })
    public var rightMargin: Int by obj.gProperty("right-margin", valueRestriction = { it >= 0 })
    public var accumulativeMargin: Boolean by obj.gProperty("accumulative-margin")

    public var pixelsAboveLines: Int by obj.gProperty("pixels-above-lines", valueRestriction = { it >= 0 })
    public var pixelsBelowLines: Int by obj.gProperty("pixels-below-lines", valueRestriction = { it >= 0 })
    public var pixelsInsideWrap: Int by obj.gProperty("pixels-inside-wrap", valueRestriction = { it >= 0 })
    public var rise: Int by obj.gProperty("rise")

    public var scale: Double by obj.gProperty("scale")
    public var size: Int by obj.gProperty("size")

    public var stretch: FontStretch by obj.gProperty("stretch") {
        FontStretch.values()[it.asEnumOrdinal]
    }
    public var style: FontStyle by obj.gProperty("style") {
        FontStyle.values()[it.asEnumOrdinal]
    }
    public var weight: Int by obj.gProperty("weight")
    public var letterSpacing: Int by obj.gProperty("letter-spacing", valueRestriction = { it >= 0 })
    public var strikethrough: Boolean by obj.gProperty("strikethrough")

    public var wrapMode: WrapMode by obj.gProperty("wrap-mode") { WrapMode.values()[it.asEnumOrdinal] }
    public var justification: Justify by obj.gProperty("justification") {
        Justify.values()[it.asEnumOrdinal]
    }

    public var invisible: Boolean by obj.gProperty("invisible")
}

@WidgetDsl
public inline fun textTag(name: String? = null, config: TextTag.() -> Unit = {}): TextTag {
    return TextTag(name).apply(config)
}

@file:Suppress("unused")

package ux

import kotlinx.cinterop.convert
import libgtk.*

public enum class Orientation(internal val gtkValue: GtkOrientation) {
    Horizontal(GtkOrientation.GTK_ORIENTATION_HORIZONTAL),
    Vertical(GtkOrientation.GTK_ORIENTATION_VERTICAL),
}

public enum class ReliefStyle(internal val gtkValue: GtkReliefStyle) {
    Normal(GtkReliefStyle.GTK_RELIEF_NORMAL),
    Half(GtkReliefStyle.GTK_RELIEF_HALF),
    None(GtkReliefStyle.GTK_RELIEF_NONE),

    ;

    internal companion object {
        internal infix fun from(gtkValue: GtkReliefStyle): ReliefStyle = when (gtkValue) {
            GtkReliefStyle.GTK_RELIEF_NORMAL -> Normal
            GtkReliefStyle.GTK_RELIEF_HALF -> Half
            GtkReliefStyle.GTK_RELIEF_NONE -> None
        }
    }
}

public enum class EventMask(internal val gtkValue: GdkEventMask) {
    KeyPress(GDK_KEY_PRESS_MASK),
    KeyRelease(GDK_KEY_RELEASE_MASK),
    EnterNotify(GDK_ENTER_NOTIFY_MASK),
    LeaveNotify(GDK_LEAVE_NOTIFY_MASK),
    PointerMotion(GDK_POINTER_MOTION_MASK),
    PointerMotionHint(GDK_POINTER_MOTION_HINT_MASK),
    PropertyChange(GDK_PROPERTY_CHANGE_MASK),
    ProximityIn(GDK_PROXIMITY_IN_MASK),
    ProximityOut(GDK_PROXIMITY_OUT_MASK),
    Scroll(GDK_SCROLL_MASK),
    SmoothScroll(GDK_SMOOTH_SCROLL_MASK),
    Structure(GDK_STRUCTURE_MASK),
    Substructure(GDK_SUBSTRUCTURE_MASK),
    TabletPad(GDK_TABLET_PAD_MASK),
    TouchpadGesture(GDK_TOUCHPAD_GESTURE_MASK),
    Touch(GDK_TOUCH_MASK),
    VisibilityNotify(GDK_VISIBILITY_NOTIFY_MASK),
    AllEvents(GDK_ALL_EVENTS_MASK),
    ButtonMotion(GDK_BUTTON_MOTION_MASK),
    Button1Motion(GDK_BUTTON1_MOTION_MASK),
    Button2Motion(GDK_BUTTON2_MOTION_MASK),
    Button3Motion(GDK_BUTTON3_MOTION_MASK),
    ButtonPress(GDK_BUTTON_PRESS_MASK),
    ButtonRelease(GDK_BUTTON_RELEASE_MASK),
    Exposure(GDK_EXPOSURE_MASK),
    FocusChange(GDK_FOCUS_CHANGE_MASK),
    ;

    internal companion object {
        internal infix fun from(gtkValue: Int): EventMask {
            val c = gtkValue.convert<UInt>()
            return values().first { it.gtkValue == c }
        }
    }
}

public enum class Align(internal val gtkValue: GtkAlign) {
    Fill(GtkAlign.GTK_ALIGN_FILL),
    Start(GtkAlign.GTK_ALIGN_START),
    End(GtkAlign.GTK_ALIGN_END),
    Center(GtkAlign.GTK_ALIGN_CENTER),
    Baseline(GtkAlign.GTK_ALIGN_BASELINE),

    ;

    internal companion object {
        internal infix fun from(gtkValue: GtkAlign): Align {
            return values()[gtkValue.ordinal]
        }
    }
}

public enum class Justify(internal val gtkValue: GtkJustification) {
    Left(GtkJustification.GTK_JUSTIFY_LEFT),
    Right(GtkJustification.GTK_JUSTIFY_RIGHT),
    Center(GtkJustification.GTK_JUSTIFY_CENTER),
    Fill(GtkJustification.GTK_JUSTIFY_FILL)

    ;

    internal companion object {
        internal infix fun from(gtkValue: GtkJustification): Justify {
            return values()[gtkValue.ordinal]
        }
    }
}

public enum class TextDirection {
    None,
    LeftToRight,
    RightToLeft,

    ;

    internal inline val gtkValue: GtkTextDirection get() = GtkTextDirection.values()[ordinal]

    internal companion object {
        internal infix fun from(gtkValue: GtkTextDirection): TextDirection = values()[gtkValue.ordinal]
    }
}

public enum class EllipsizeMode {
    None,
    Start,
    Middle,
    End;

    internal inline val gtkValue: PangoEllipsizeMode get() = PangoEllipsizeMode.values()[ordinal]

    internal companion object {
        internal infix fun from(gtkValue: PangoEllipsizeMode): EllipsizeMode = values()[gtkValue.ordinal]
    }
}

public enum class WrapMode {
    Word,
    Character,
    WordCharacter;

    internal inline val gtkValue: PangoWrapMode get() = PangoWrapMode.values()[ordinal]

    internal companion object {
        internal infix fun from(gtkValue: PangoWrapMode): WrapMode = values()[gtkValue.ordinal]
    }
}

public enum class PackType {
    Start,
    End;

    internal inline val gtkValue: GtkPackType get() = GtkPackType.values()[ordinal]

    internal companion object {
        internal infix fun from(gtkValue: GtkPackType): PackType = values()[gtkValue.ordinal]
    }
}

public enum class FontStretch {
    UltraCondensed,
    ExtraCondensed,
    Condensed,
    SemiCondensed,
    Normal,
    SemiExpanded,
    Expanded,
    ExtraExpanded,
    UltraExpanded,
}

public enum class FontStyle {
    Normal,
    Oblique,
    Italic
}

public enum class UnderlineStyle {
    None,
    Single,
    Double,
    Low,
    Error,
    SingleLine,
    DoubleLine,
    ErrorLine,
}

public enum class FontVariant {
    Normal,
    SmallCaps
}

public object FontWeight {
    public const val Thin: Int = 100
    public const val Ultralight: Int = 200
    public const val Light: Int = 300
    public const val Semilight: Int = 350
    public const val Book: Int = 380
    public const val Normal: Int = 400
    public const val Medium: Int = 500
    public const val Semibold: Int = 600
    public const val Bold: Int = 700
    public const val UltraBold: Int = 800
    public const val Heavy: Int = 900
    public const val UltraHeavy: Int = 1000
}

public object FontScale {
    public const val Tiny: Double = 0.5787037037037
    public const val ExtraSmall: Double = 0.6944444444444
    public const val Small: Double = 0.8333333333333
    public const val Normal: Double = 1.0
    public const val Large: Double = 1.2
    public const val ExtraLarge: Double = 1.44
    public const val Huge: Double = 1.728
}

public enum class PositionType {
    Left,
    Right,
    Top,
    Bottom
    ;

    internal inline val gtkValue: GtkPositionType get() = GtkPositionType.values()[ordinal]

    internal companion object {
        internal infix fun from(gtkValue: GtkPositionType): PositionType = values()[gtkValue.ordinal]
    }
}

public enum class ShadowType {
    None,
    In,
    Out,
    EtchedIn,
    EtchedOut
    ;

    internal inline val gtkValue: GtkShadowType get() = GtkShadowType.values()[ordinal]

    internal companion object {
        internal infix fun from(gtkValue: GtkShadowType): ShadowType = values()[gtkValue.ordinal]
    }
}

public enum class SelectionMode {
    None,
    Single,
    Browse,
    Multiple
    ;

    internal companion object {
        infix fun from(gtkValue: GtkSelectionMode) = values()[gtkValue.ordinal]
    }

    internal val gtkValue get() = GtkSelectionMode.values()[ordinal]
}

public enum class GlibType {
    Invalid,
    None,
    Interface,
    Char,
    UChar,
    Boolean,
    Int,
    UInt,
    Long,
    ULong,
    Int64,
    UInt64,
    Enum,
    Flags,
    Float,
    Double,
    String,
    Pointer,
    Boxed,
    Param,
    Object,
    Variant;

    internal val gtkValue: GType get() = (ordinal shl G_TYPE_FUNDAMENTAL_SHIFT).convert()
}

public enum class LicenseType {
    Unknown,
    Custom,
    GPL_2_0,
    GPL_3_0,
    LGPL_2_1,
    LGPL_3_0,
    BSD,
    MIT_X11,
    Artistic,
    GPL_2_0_ONLY,
    GPL_3_0_ONLY,
    LGPL_2_1_ONLY,
    LGPL_3_0_ONLY,
    AGPL_3_0,

    ;

    internal val gtkValue: GtkLicense = GtkLicense.values()[ordinal]

    internal companion object {
        internal infix fun from(gtkValue: GtkLicense) = values()[gtkValue.ordinal]
    }
}

public enum class TextWindowType {
    Widget,
    Text,
    Left,
    Right,
    Top,
    Bottom;

    internal companion object {
        internal infix fun from(gtkValue: GtkTextWindowType): TextWindowType = values()[gtkValue.ordinal]
    }
    internal val gtkValue: GtkTextWindowType = GtkTextWindowType.values()[ordinal]
}

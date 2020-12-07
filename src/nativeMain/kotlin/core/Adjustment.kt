@file:Suppress("unused")

package ux


import libgtk.*

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef

public typealias DoubleRange = ClosedFloatingPointRange<Double>

public class Adjustment internal constructor(internal val self: CPointer<GtkAdjustment>) {
    public constructor(
        value: Double,
        range: DoubleRange,
        stepIncrement: Double,
        pageIncrement: Double,
        pageSize: Double,
    ) : this(gtk_adjustment_new(value, range.start, range.endInclusive, stepIncrement, pageIncrement, pageSize)!!)

    public var value: Double
        get() = gtk_adjustment_get_value(self)
        set(value) {
            gtk_adjustment_set_value(self, value)
        }

    public var lower: Double
        get() = gtk_adjustment_get_lower(self)
        set(value) {
            gtk_adjustment_set_lower(self, value)
        }

    public var upper: Double
        get() = gtk_adjustment_get_upper(self)
        set(value) {
            gtk_adjustment_set_upper(self, value)
        }

    public var pageIncrement: Double
        get() = gtk_adjustment_get_page_increment(self)
        set(value) {
            gtk_adjustment_set_page_increment(self, value)
        }

    public var pageSize: Double
        get() = gtk_adjustment_get_page_size(self)
        set(value) {
            gtk_adjustment_set_page_size(self, value)
        }

    public var stepIncrement: Double
        get() = gtk_adjustment_get_step_increment(self)
        set(value) {
            gtk_adjustment_set_step_increment(self, value)
        }

    public val minimumIncrement: Double
        get() = gtk_adjustment_get_minimum_increment(self)


    public fun clampPage(range: DoubleRange) {
        gtk_adjustment_clamp_page(self, range.start, range.endInclusive)
    }

    public fun configure(
        value: Double,
        range: DoubleRange,
        stepIncrement: Double,
        pageIncrement: Double,
        pageSize: Double,
    ) {
        gtk_adjustment_configure(
            self,
            value,
            range.start,
            range.endInclusive,
            stepIncrement,
            pageIncrement,
            pageSize
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////
    public fun onChanged(callback: () -> Unit) {
        self.connectSignal(
            "changed",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    public fun onValueChanged(callback: () -> Unit) {
        self.connectSignal(
            "value-changed",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

}

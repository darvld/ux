@file:Suppress("unused")

package ux

import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret

public open class Dialog internal constructor(
    pointer: WidgetPtr
) : Window(pointer) {
    public constructor() : this(gtk_dialog_new()!!)

    private val self: CPointer<GtkDialog> = pointer.reinterpret()

    public fun run(): Int {
        return gtk_dialog_run(self)
    }

    public fun invokeResponse(code: Int) {
        gtk_dialog_response(self, code)
    }

    public fun addButton(text: String, responseCode: Int): Button? {
        val buttonPointer = gtk_dialog_add_button(self, text, responseCode) ?: return null
        return Button(buttonPointer)
    }

    public fun addButtons(vararg buttons: Pair<String, Int>): List<Button> {
        return buttons.mapNotNull { pair ->
            gtk_dialog_add_button(self, pair.first, pair.second)?.let {
                Button(it)
            }
        }
    }

    public fun addActionWidget(widget: Widget, responseCode: Int) {
        gtk_dialog_add_action_widget(self, widget.widgetPtr, responseCode)
    }

    public fun setDefaultResponse(code: ResponseCode) {
        gtk_dialog_set_default_response(self, code.value)
    }

    public fun setResponseSensitive(code: ResponseCode, sensitive: Boolean) {
        gtk_dialog_set_response_sensitive(self, code.value, sensitive.gtkValue)
    }

    public fun getResponseForWidget(widget: Widget): ResponseCode {
        return ResponseCode.from(gtk_dialog_get_response_for_widget(self, widget.widgetPtr))
    }

    public fun getWidgetForResponse(code: ResponseCode): Widget? {
        return gtk_dialog_get_widget_for_response(self, code.value)?.let { Widget(it) }
    }

    public fun getContentArea(): Box {
        return Box(gtk_dialog_get_content_area(self)!!)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////

    public fun onResponse(callback: (code: ResponseCode) -> Unit) {
        connectSignal(
            "response",
            handler = staticCallback1,
            callbackWrapper = StableRef.create { i: Int ->
                callback(ResponseCode from i)
            }.asCPointer()
        )
    }

    public fun onClose(callback: () -> Unit) {
        connectSignal(
            "close",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // Static members
    ///////////////////////////////////////////////////////////////////////////

    public sealed class ResponseCode(public val value: Int) {
        public object None : ResponseCode(-1)
        public object Reject : ResponseCode(-2)
        public object Accept : ResponseCode(-3)
        public object Delete : ResponseCode(-4)
        public object Ok : ResponseCode(-5)
        public object Cancel : ResponseCode(-6)
        public object Close : ResponseCode(-7)
        public object Yes : ResponseCode(-8)
        public object No : ResponseCode(-9)
        public object Apply : ResponseCode(-10)
        public object Help : ResponseCode(-11)

        public class Custom(code: Int) : ResponseCode(code)

        public companion object {
            public infix fun from(i: Int): ResponseCode {
                return when (i) {
                    -1 -> None
                    -2 -> Reject
                    -3 -> Accept
                    -4 -> Delete
                    -5 -> Ok
                    -6 -> Cancel
                    -7 -> Close
                    -8 -> Yes
                    -9 -> No
                    -10 -> Apply
                    -11 -> Help
                    else -> Custom(i)
                }
            }
        }
    }
}

@WidgetDsl
public fun dialog(
    title: String? = null,
    subtitle: String? = null,
    vararg buttons: Pair<String, Dialog.ResponseCode>,
    op: Dialog.() -> Unit = {}
): Dialog {
    return Dialog().also { instance ->
        instance.headerBar(title, subtitle)

        instance.addButtons(*buttons.map { it.first to it.second.value }.toTypedArray())
        instance.show()

        instance.op()
    }
}

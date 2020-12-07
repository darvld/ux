    @file:Suppress("unused")

package ux


import libgtk.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString


    public class FileChooserButton internal constructor(
    pointer: CPointer<GtkWidget>
) : Button(pointer), FileChooser {

    public constructor(
        title: String? = null,
        mode: FileChooser.Action = FileChooser.Action.Open
    ) : this(gtk_file_chooser_button_new(title, mode.gtkValue)!!)

    public constructor(
        dialog: FileChooserDialog
    ) : this(gtk_file_chooser_button_new_with_dialog(dialog.widgetPtr)!!)

    private val self: CPointer<GtkFileChooserButton> = pointer.reinterpret()

    public var title: String?
        get() = gtk_file_chooser_button_get_title(self)?.toKString()
        set(value) {
            gtk_file_chooser_button_set_title(self, value)
        }

    public var widthChars: Int
        get() = gtk_file_chooser_button_get_width_chars(self)
        set(value) {
            gtk_file_chooser_button_set_width_chars(self, value)
        }

    public inline fun onFileSet(crossinline callback: (path: String?) -> Unit) {
        val internalCallback = {
            callback(filename)
        }
        connectSignal(
            "file-set",
            callbackWrapper = StableRef.create(internalCallback).asCPointer()
        )
    }

    override val baseWidget: Widget = this
}

@WidgetDsl
public inline fun Container.fileChooserButton(
    title: String? = null,
    mode: FileChooser.Action = FileChooser.Action.Open,
    op: FileChooserButton.() -> Unit = {}
): FileChooserButton {
    require(mode == FileChooser.Action.Open || mode == FileChooser.Action.SelectFolder)

    val b = FileChooserButton(title, mode)

    add(b)
    b.show()

    b.op()
    return b
}

    @WidgetDsl
    public inline fun Container.fileChooserButton(
        dialog: FileChooserDialog,
        op: FileChooserButton.() -> Unit = {}
    ): FileChooserButton {
        val b = FileChooserButton(dialog)

        add(b)
        b.show()

        b.op()
        return b
    }

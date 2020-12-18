package ux






import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import libgtk.GtkFileChooserDialog
import libgtk.GtkWidget
import libgtk.gtk_file_chooser_dialog_new

public open class FileChooserDialog(
    pointer: WidgetPtr
) : Dialog(pointer), FileChooser {
    public constructor(
        title: String? = null,
        parent: Window? = null,
        action: FileChooser.Action = FileChooser.Action.Open
    ) : this(gtk_file_chooser_dialog_new(title,parent?.widgetPtr?.reinterpret(), action.gtkValue,null)!!)

    internal val self: CPointer<GtkFileChooserDialog> = pointer.reinterpret()

    override val baseWidget: Widget by lazy { Widget(widgetPtr) }
}

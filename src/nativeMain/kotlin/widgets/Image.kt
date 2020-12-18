@file:Suppress("unused")

package ux

import kotlinx.cinterop.*
import libgtk.*
import ux.Pixbuf
import ux.Widget

public class Image(pointer: WidgetPtr) : Widget(pointer) {
    public constructor() : this(gtk_image_new()!!)
    public constructor(file: String) : this(gtk_image_new_from_file(file)!!)
    public constructor(pixbuf: Pixbuf) : this(gtk_image_new_from_pixbuf(pixbuf.cpointer)!!)
    public constructor(iconName: String, size: GtkIconSize) : this(gtk_image_new_from_icon_name(iconName, size)!!)

    private val self: CPointer<GtkImage> = pointer.reinterpret()

    public val pixbuf: Pixbuf?
        get() = gtk_image_get_pixbuf(self)?.let { Pixbuf(it) }

    public val iconName: String?
        get() = memScoped {
            val name = allocPointerTo<ByteVar>().ptr
            gtk_image_get_icon_name(self, name, null)

            return name.pointed.value?.toKString()
        }

    public var pixelSize: Int
        get() = gtk_image_get_pixel_size(self)
        set(value) {
            gtk_image_set_pixel_size(self, value)
        }
    
    public fun clear() {
        gtk_image_clear(self)
    }

    public val storageType: GtkImageType
        get() = gtk_image_get_storage_type(self)

    public fun setFromFile(file: String) {
        gtk_image_set_from_file(self, file)
    }

    public fun setFromIconName(iconName: String, size: GtkIconSize) {
        gtk_image_set_from_icon_name(self, iconName, size)
    }

    public fun setFromPixbuf(pixbuf: Pixbuf) {
        gtk_image_set_from_pixbuf(self, pixbuf.cpointer)
    }
}

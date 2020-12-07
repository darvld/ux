@file:Suppress("unused")

package ux




import libgtk.*
import kotlinx.cinterop.*

public open class AboutDialog(
    pointer: CPointer<GtkWidget>
) : Dialog(pointer) {
    public constructor() : this(gtk_about_dialog_new()!!)

    public var programName: String
        get() = gtk_about_dialog_get_program_name(self)?.toKString() ?: ""
        set(value) {
            gtk_about_dialog_set_program_name(self, value)
        }

    public var version: String
        get() = gtk_about_dialog_get_version(self)?.toKString() ?: ""
        set(value) {
            gtk_about_dialog_set_version(self, value)
        }

    public var copyright: String
        get() = gtk_about_dialog_get_copyright(self)?.toKString() ?: ""
        set(value) {
            gtk_about_dialog_set_copyright(self, value)
        }

    public var comments: String
        get() = gtk_about_dialog_get_comments(self)?.toKString() ?: ""
        set(value) {
            gtk_about_dialog_set_comments(self, value)
        }

    public var license: String
        get() = gtk_about_dialog_get_license(self)?.toKString() ?: ""
        set(value) {
            gtk_about_dialog_set_license(self, value)
        }

    public var wrapLicense: Boolean
        get() = Boolean from gtk_about_dialog_get_wrap_license(self)
        set(value) {
            gtk_about_dialog_set_wrap_license(self, value.gtkValue)
        }

    public var licenseType: LicenseType
        get() = LicenseType from gtk_about_dialog_get_license_type(self)
        set(value) {
            gtk_about_dialog_set_license_type(self, value.gtkValue)
        }

    public var website: String
        get() = gtk_about_dialog_get_website(self)?.toKString() ?: ""
        set(value) {
            gtk_about_dialog_set_website(self, value)
        }

    public var websiteLabel: String
        get() = gtk_about_dialog_get_website_label(self)?.toKString() ?: ""
        set(value) {
            gtk_about_dialog_set_website_label(self, value)
        }

    public var authors: List<String>
        get() = gtk_about_dialog_get_authors(self)?.toStringList() ?: emptyList()
        set(value) = memScoped {
            val cArray = allocArrayOf(value.map { it.cstr.ptr })
            gtk_about_dialog_set_authors(self, cArray)
        }

    public var artists: List<String>
        get() = gtk_about_dialog_get_artists(self)?.toStringList() ?: emptyList()
        set(value) = memScoped {
            val cArray = allocArrayOf(value.map { it.cstr.ptr })
            gtk_about_dialog_set_artists(self, cArray)
        }

    public var documenters: List<String>
        get() = gtk_about_dialog_get_documenters(self)?.toStringList() ?: emptyList()
        set(value) = memScoped {
            val cArray = allocArrayOf(value.map { it.cstr.ptr })
            gtk_about_dialog_set_documenters(self, cArray)
        }

    public var translatorCredits: String
        get() = gtk_about_dialog_get_translator_credits(self)?.toKString() ?: ""
        set(value) {
            gtk_about_dialog_set_translator_credits(self, value)
        }

    public var logo: Pixbuf?
        get() = gtk_about_dialog_get_logo(self)?.let { Pixbuf(it) }
        set(value) {
            gtk_about_dialog_set_logo(self, value?.cpointer)
        }

    public var logoIconName: String?
        get() = gtk_about_dialog_get_logo_icon_name(self)?.toKString()
        set(value) {
            gtk_about_dialog_set_logo_icon_name(self, value)
        }

    public fun addCreditSection(sectionName: String, people: List<String>): Unit = memScoped {
        gtk_about_dialog_add_credit_section(self, sectionName, allocArrayOf(people.map { it.cstr.ptr }))
    }

    private val self: CPointer<GtkAboutDialog> = pointer.reinterpret()
}

public fun CPointer<CPointerVar<ByteVar>>.toStringList(): List<String> {
    val list = mutableListOf<String>()
    var i = 0
    while (true) {
        list.add(this[i]?.toKString() ?: break)
        i++
    }

    return list
}

@WidgetDsl
public fun aboutDialog(
    setup: AboutDialog.() -> Unit = {}
): AboutDialog {
    val dialog = AboutDialog()

    dialog.show()
    dialog.setup()

    return dialog
}

@file:Suppress("unused")

package ux

import libgtk.*

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString


public interface FileChooser  : ExtensionInterface{
    public enum class Action {
        Open,
        Save,
        SelectFolder,
        CreateFolder
        ;

        internal inline val gtkValue: GtkFileChooserAction get() = GtkFileChooserAction.values()[ordinal]

        internal companion object {
            internal inline infix fun from(gtkValue: GtkFileChooserAction): Action = values()[gtkValue.ordinal]
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// Methods
///////////////////////////////////////////////////////////////////////////

public var Scrollable.action: FileChooser.Action
    get() = FileChooser.Action from gtk_file_chooser_get_action(baseWidget.widgetPtr.reinterpret())
    set(value) {
        gtk_file_chooser_set_action(baseWidget.widgetPtr.reinterpret(), value.gtkValue)
    }

public var Scrollable.localOnly: Boolean
    get() = Boolean from gtk_file_chooser_get_local_only(baseWidget.widgetPtr.reinterpret())
    set(value) {
        gtk_file_chooser_set_local_only(baseWidget.widgetPtr.reinterpret(), value.gtkValue)
    }

public var FileChooser.selectMultiple: Boolean
    get() = Boolean from gtk_file_chooser_get_select_multiple(baseWidget.widgetPtr.reinterpret())
    set(value) {
        gtk_file_chooser_set_select_multiple(baseWidget.widgetPtr.reinterpret(), value.gtkValue)
    }

public var FileChooser.showHidden: Boolean
    get() = Boolean from gtk_file_chooser_get_show_hidden(baseWidget.widgetPtr.reinterpret())
    set(value) {
        gtk_file_chooser_set_show_hidden(baseWidget.widgetPtr.reinterpret(), value.gtkValue)
    }

public var FileChooser.overwriteConfirmation: Boolean
    get() = Boolean from gtk_file_chooser_get_do_overwrite_confirmation(baseWidget.widgetPtr.reinterpret())
    set(value) {
        gtk_file_chooser_set_do_overwrite_confirmation(baseWidget.widgetPtr.reinterpret(), value.gtkValue)
    }

public var FileChooser.createFolders: Boolean
    get() = Boolean from gtk_file_chooser_get_create_folders(baseWidget.widgetPtr.reinterpret())
    set(value) {
        gtk_file_chooser_set_create_folders(baseWidget.widgetPtr.reinterpret(), value.gtkValue)
    }

public var FileChooser.currentName: String?
    get() = gtk_file_chooser_get_current_name(baseWidget.widgetPtr.reinterpret())?.toKString()
    set(value) {
        gtk_file_chooser_set_current_name(baseWidget.widgetPtr.reinterpret(), value)
    }

public var FileChooser.filename: String?
    get() = gtk_file_chooser_get_filename(baseWidget.widgetPtr.reinterpret())?.toKString()
    set(value) {
        gtk_file_chooser_set_filename(baseWidget.widgetPtr.reinterpret(), value)
    }

public val FileChooser.filenames: List<String>
    get() {
        val names = gtk_file_chooser_get_filenames(baseWidget.widgetPtr.reinterpret()).asSequence<ByteVar>()
        return names.map { cString ->
            cString.toKString().also { g_free(cString) }
        }.toList()
    }

public var FileChooser.currentFolder: String?
    get() = gtk_file_chooser_get_current_folder(baseWidget.widgetPtr.reinterpret())?.toKString()
    set(value) {
        gtk_file_chooser_set_current_folder(baseWidget.widgetPtr.reinterpret(), value)
    }

public var FileChooser.uri: String?
    get() = gtk_file_chooser_get_uri(baseWidget.widgetPtr.reinterpret())?.toKString()
    set(value) {
        gtk_file_chooser_set_uri(baseWidget.widgetPtr.reinterpret(), value)
    }

public var FileChooser.currentFolderUri: String?
    get() = gtk_file_chooser_get_current_folder_uri(baseWidget.widgetPtr.reinterpret())?.toKString()
    set(value) {
        gtk_file_chooser_set_current_folder_uri(baseWidget.widgetPtr.reinterpret(), value)
    }

public fun FileChooser.selectUri(uri: String) {
    gtk_file_chooser_select_uri(baseWidget.widgetPtr.reinterpret(), uri)
}

public fun FileChooser.unselectUri(uri: String) {
    gtk_file_chooser_unselect_uri(baseWidget.widgetPtr.reinterpret(), uri)
}

public fun FileChooser.selectFilename(name: String) {
    gtk_file_chooser_select_filename(baseWidget.widgetPtr.reinterpret(), name)
}

public fun FileChooser.unselectFilename(name: String) {
    gtk_file_chooser_unselect_filename(baseWidget.widgetPtr.reinterpret(), name)
}

public fun FileChooser.selectAll() {
    gtk_file_chooser_select_all(baseWidget.widgetPtr.reinterpret())
}

public fun FileChooser.unselectAll() {
    gtk_file_chooser_unselect_all(baseWidget.widgetPtr.reinterpret())
}

///////////////////////////////////////////////////////////////////////////
// Signals
///////////////////////////////////////////////////////////////////////////
public fun FileChooser.onCurrentFolderChanged(callback: () -> Unit) {
    baseWidget.connectSignal(
        "current-folder-changed",
        callbackWrapper = StableRef.create(callback).asCPointer()
    )
}


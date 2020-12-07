@file:Suppress("unused", "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate")

package ux

import libgtk.*
import kotlinx.cinterop.*

/**An object representing an application instance. This class is a wrapper for [GtkApplication]. This class handles
 *  most top-level events and commands (initialization, session management, etc.).
 *
 * The [application] DSL method is the recommended way to create instances of this class.
 *
 * For more information, see the GTK reference for [GtkApplication][http://developer.gnome.org/gtk3/].
 * */
@WidgetDsl
public open class Application internal constructor(internal val cpointer: CPointer<GtkApplication>) {
    public constructor(id: String?, vararg options: Option) : this(
        gtk_application_new(
            id,
            // Fold all options using a bitwise "or" operator to collapse them into a single Int
            options.fold(0) { flags, opt -> flags or opt.value }.convert()
        )!!
    )

    /**Options used during the creation of an [Application] instance. Internally, this enum's values correspond to
     *  the ones defined by Glib for [GApplicationFlags][https://developer.gnome.org/gio/unstable/GApplication.html#GApplicationFlags].*/
    public enum class Option {
        /**Run as a service. In this mode, registration
         * fails if the service is already running, and the application
         * will initially wait up to 10 seconds for an initial activation
         * message to arrive..*/
        Service,

        /**Don't try to become the primary instance.*/
        Launcher,

        /**This application handles opening files (in the primary instance). Note that this flag only affects
         *  the default implementation of local_command_line(), and has no effect if [HandlesCommandLine]
         *  is given.*/
        HandlesOpen,

        /**This application handles command line
         *     arguments (in the primary instance). Note that this flag only affects
         *     the default implementation of local_command_line().*/
        HandlesCommandLine,

        /**Send the environment of the
         * launching process to the primary instance.
         *
         * Set this flag if your application is expected to behave differently depending on certain environment variables.
         * For instance, an editor might be expected to use the `GIT_COMMITTER_NAME` environment variable
         * when editing a git commit message.
         *
         * The environment is available
         * to the #GApplication::command-line signal handler, via
         * g_application_command_line_getenv().*/
        SendEnvironment,

        /**Make no attempts to do any of the typical
         *     single-instance application negotiation, even if the application
         *     ID is given.  The application neither attempts to become the
         *     owner of the application ID nor does it check if an existing
         *     owner already exists.  Everything occurs in the local process.*/
        NonUnique,

        /**Allow users to override the
         *     application ID from the command line with `--gapplication-app-id`.*/
        CanOverrideAppId,

        /**Allow another instance to take over the bus name.*/
        AllowReplacement,

        /** Take over from another instance. This flag is
         *     usually set by passing `--gapplication-replace` on the commandline..*/
        Replace
        ;

        public val value: Int get() = 1 shl ordinal
    }

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    /**A list of all [Windows][Window] associated with this [Application].

    The list is sorted by most recently focused window, such that the first element is the currently focused window and
    will only remain valid until the next focus change or window creation or deletion.*/
    public val windows: List<Window>
        get() {
            val cList = gtk_application_get_windows(cpointer)
            return cList.asSequence<GtkWindow>().map {
                Window(it.reinterpret())
            }.toList()
        }

    /**Gets the “active” window on this [Application].

    The active window is the one that was most recently focused (within the application). This window may not have the
    focus at the moment if another application has it — this is just the most recently-focused window within this application.*/
    public val activeWindow: Window
        get() {
            return Window(gtk_application_get_active_window(cpointer)!!.reinterpret())
        }

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    /**Launches this application.
     *
     * Note that this operation blocks the current thread until the application's execution is complete.
     * Once finished, the exit code supplied by GTK is returned from this method.
     *
     * A GTK application usually returns once no windows are visible.*/
    public fun run(): Int {
        val status = memScoped {
            g_application_run(cpointer.reinterpret(), 0, null)
        }
        g_object_unref(cpointer)

        return status
    }

    /**Connects the given [callback] to the "activate" signal of this application, usually received after the [run] method
     * is called.
     *
     * Use this method to initialize your application and add any initial windows and components you may need.*/
    public fun onActivate(callback: () -> Unit) {
        cpointer.connectSignal(
            "activate",
            handler = staticCallback,
            callbackWrapper = StableRef.create(callback).asCPointer()
        )
    }

    /**Adds a [Window] to this [Application].

    This call can only happen after the application has started; typically, you should add new application windows in
    response to the emission of the “activate” signal (or using the lambda scope of the [application] DSL method).

    Normally, the connection between the application and the window will remain until the window is destroyed,
    but you can explicitly remove it with [removeWindow].

    GTK+ will keep the application running as long as it has any windows.*/
    public fun addWindow(window: Window) {
        gtk_application_add_window(cpointer, window.widgetPtr.reinterpret())
    }

    /**Remove a window from this application.

    The application may stop running as a result of a call to this function if there are no other windows connected to it.*/
    public fun removeWindow(window: Window) {
        gtk_application_remove_window(cpointer, window.widgetPtr.reinterpret())
    }

    /**Returns a [Window] with the given [id], or null if there is no window with that id assigned.*/
    public fun getWindowById(id: Int): Window? {
        return gtk_application_get_window_by_id(cpointer, id.convert())?.let {
            Window(it.reinterpret())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Signals
    ///////////////////////////////////////////////////////////////////////////
    /**Signal emitted when a [Window] is added to this application.*/
    public fun onWindowAdded(callback: (Window) -> Unit) {
        cpointer.connectSignal(
            signal = "window-added",
            handler = staticCallback1,
            callbackWrapper = StableRef.create { pointer: CPointer<GtkWidget> ->
                callback(Window(pointer))
            }.asCPointer(),
        )
    }

    /**Signal emitted when a [Window] is removed from this application.*/
    public fun onWindowRemoved(callback: (Window) -> Unit) {
        cpointer.connectSignal(
            signal = "window-removed",
            handler = staticCallback1,
            callbackWrapper = StableRef.create { pointer: CPointer<GtkWidget> ->
                callback(Window(pointer))
            }.asCPointer(),
        )
    }
}

@WidgetDsl
/**DSL for creating an [Application] instance and configuring it on the same function. The supplied
 * [options] are passed on to GTK during the instance creation and the [init] block is connected to the application's
 * "activate" signal.
 *
 *```
 *public fun main() {
 *  application("com.example.app"){
 *      applicationWindow {
 *          ...
 *      }
 *
 *      ...
 *  }
 *}
 *```*/
public fun application(
    id: String,
    vararg options: Application.Option,
    init: Application.() -> Unit
): Application {
    return Application(
        gtk_application_new(
            id,
            // Fold all options using a bitwise "or" operator to collapse them into a single Int
            options.fold(0) { flags, opt -> flags or opt.value }.convert()
        )!!
    ).apply { onActivate { init() } }
}




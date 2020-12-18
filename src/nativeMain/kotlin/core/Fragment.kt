@file:Suppress("MemberVisibilityCanBePrivate")

package core

import kotlinx.cinterop.Arena
import ux.Container
import ux.Widget

public abstract class Fragment {
    protected val arena: Arena by lazy { Arena() }
    protected lateinit var host: FragmentHost

    public lateinit var root: Widget

    internal fun init(fragmentHost: FragmentHost): Widget {
        host = fragmentHost
        root = createView(fragmentHost)

        onAttached()

        return root
    }

    internal fun destroy() {
        onDestroy()
        arena.clear()
        root.destroy()
    }

    public abstract fun createView(container: Container): Widget

    public open fun onAttached() {}

    public open fun onDisplay() {}

    public open fun onDestroy() {}

    public fun navigateTo(destination: Fragment) {
        host.navigateTo(destination)
    }

    public fun navigateTo(destination: () -> Fragment) {
        navigateTo(destination())
    }

    public fun navigateUp() {
        host.navigateUp()
    }
}

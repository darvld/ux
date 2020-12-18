package core

import ux.Container
import ux.Stack

public inline fun Container.fragmentHost(
    setup: FragmentHost.() -> Unit
): FragmentHost = FragmentHost().apply {
    this@fragmentHost.add(this)
    show()
    setup()
}

public class FragmentHost : Stack() {
    private val navigationStack = mutableListOf<Fragment>()

    public fun rootFragment(factory: () -> Fragment) {
        factory().let {
            navigationStack.add(0, it)
            visibleChild = it.init(this)
            navigateTo(it)
        }
    }

    public fun navigateUp() {
        require(navigationStack.isNotEmpty()) { "Navigation stack must not be empty when attempting to navigate up" }

        // Avoid removing the root fragment
        if (navigationStack.size == 1) return

        navigationStack.removeLastOrNull()?.let {
            visibleChild = navigationStack.last().root
            it.destroy()
        }
    }

    public fun navigateTo(fragment: Fragment) {
        if (navigationStack.contains(fragment)) {
            visibleChild = fragment.root
        } else {
            visibleChild = fragment.init(this)
            navigationStack.add(fragment)
        }

        fragment.onDisplay()
    }
}

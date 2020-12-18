@file:Suppress("unused")

package ux

import libgtk.GtkIconSize

public inline fun Container.primaryButton(label: String? = null, crossinline setup: Button.() -> Unit = {}): Button =
    button {
        addStyleClass("primary")
        label?.let {
            this.label = it.toUpperCase()
        }
        setup(this)
    }

public inline fun Container.outlinedButton(label: String? = null, crossinline setup: Button.() -> Unit = {}): Button =
    button {
        addStyleClass("outlined")
        label?.let {
            this.label = it.toUpperCase()
        }
        setup(this)
    }

public inline fun Container.textButton(label: String? = null, crossinline setup: Button.() -> Unit = {}): Button =
    button {
        addStyleClass("text")
        label?.let {
            this.label = it.toUpperCase()
        }
        setup(this)
    }

public inline fun Container.actionButton(imageName: String, crossinline setup: Button.() -> Unit = {}): Button =
    button {
        addStyleClass("action")
        image = Image(imageName, GtkIconSize.GTK_ICON_SIZE_BUTTON)
        setup()
    }

public inline fun Label.subtitle() {
    addStyleClass("subtitle")
}

public inline fun Label.subtitle2() {
    addStyleClass("subtitle2")
}

public inline fun Label.overline() {
    text = text.toUpperCase()
    addStyleClass("overline")
}

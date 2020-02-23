package eu.bunburya.simugration.view

import tornadofx.*

class MainView : View("Simugration") {
    override val root = borderpane {
        top(TopMenuView::class)
        center(MainMenuView::class)
        bottom(StatusBarView::class)
    }
}

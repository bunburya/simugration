package eu.bunburya.simugration.view

import tornadofx.*

class TopMenuView : View("Menu") {
    override val root = menubar {
        menu("File") {
            item("Launch simulation").action {
                find(MainView::class).root.center(LauncherView::class)
            }
            item("Quit").action { System.exit(0) }
        }
    }
}

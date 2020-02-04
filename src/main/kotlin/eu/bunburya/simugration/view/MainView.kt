package eu.bunburya.simugration.view

import tornadofx.*

class MainView : View("Simugration") {
    override val root = borderpane {
        println(System.getProperty("java.version"))
        top(MainMenuView::class)
    }
}

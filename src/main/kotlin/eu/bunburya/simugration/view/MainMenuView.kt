package eu.bunburya.simugration.view

import tornadofx.*
import tornadofx.ListMenu
import tornadofx.ListMenuItem

class MainMenuView : View("Main Menu") {
    override val root = listmenu {
        item(text = "Launch Simulation") {
            //activeItem = this
            whenSelected {
                //replaceWith<LauncherView>()
                LauncherView().openWindow()
            }
        }
        item (text = "Quit") {
            whenSelected {
                System.exit(0)
            }
        }
    }

}

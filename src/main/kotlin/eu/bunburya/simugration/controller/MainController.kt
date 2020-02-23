package eu.bunburya.simugration.controller

import eu.bunburya.simugration.view.SimulationView
import eu.bunburya.simugration.view.StatusBarView
import eu.bunburya.simugration.view.TopMenuView
import tornadofx.Controller

class MainController: Controller() {

    val topMenuView: TopMenuView by inject()
    val simulationView: SimulationView by inject()
    val statusBarView: StatusBarView by inject()

    fun setStatus(text: String) {
        statusBarView.root.text = text
    }

}
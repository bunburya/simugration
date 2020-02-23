package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.GridAspect
import eu.bunburya.simugration.controller.SimulationController
import tornadofx.*

class SimulationView : View("Simulation") {
    private val controller: SimulationController by inject()

    override val root = borderpane {
        center(GridView::class)
        right(SimControlView::class)
        bottom(SimInfoView::class)
    }
}

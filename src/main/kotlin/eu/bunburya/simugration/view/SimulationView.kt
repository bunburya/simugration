package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.GridAspect
import eu.bunburya.simugration.controller.SimulationController
import tornadofx.*

class SimulationView : View("Simulation") {
    private val controller: SimulationController by inject()

    override val root = borderpane {
        top = hbox {
            label("View:")
            button("Population").setOnAction { controller.draw(GridAspect.POPULATION) }
            button("Resources").setOnAction { controller.draw(GridAspect.RESOURCES) }
            button("Elevation").setOnAction { controller.draw(GridAspect.ELEVATION) }
            button("Next step").setOnAction { controller.step() }
        }
        center(GridView::class)
        bottom(SimInfoView::class)
    }
}

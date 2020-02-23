package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.GridAspect
import eu.bunburya.simugration.controller.SimulationController
import tornadofx.*

class SimControlView : View("My View") {
    val controller: SimulationController by inject()

    override val root = vbox {
        label("View")
        button("Population").setOnAction { controller.draw(GridAspect.POPULATION) }
        button("Resources").setOnAction { controller.draw(GridAspect.RESOURCES) }
        button("Elevation").setOnAction { controller.draw(GridAspect.ELEVATION) }
        button("Desirability").setOnAction { controller.draw(GridAspect.DESIRABILITY) }
        separator()
        label("Simulation")
        button("Next step").setOnAction { controller.step() }
    }
}

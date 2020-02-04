package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.SimulationController
import tornadofx.*

class SimulationView : View("Simulation") {
    private val controller: SimulationController by inject()

    override val root = borderpane {
        top = hbox {
            label("View:")
            button("Population").setOnAction { controller.drawPopulation() }
            button("Resources").setOnAction { controller.drawResources() }
            button("Terrain").setOnAction { controller.drawTerrain() }
        }
        center(GridView::class)
        bottom(SimInfoView::class)
    }
}

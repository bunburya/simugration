package eu.bunburya.simugration.controller

import eu.bunburya.simugration.model.config.SimConfig
import eu.bunburya.simugration.view.LauncherView
import eu.bunburya.simugration.view.SimulationView
import tornadofx.Controller

/**
 * Controller to launch a new simulation.  Tied to a LauncherView which takes user input to determine parameters of
 * simulation.
 */

class LauncherController: Controller() {

    val view: LauncherView by inject()
    val simulationController: SimulationController by inject()
    val simConfig = SimConfig()

    fun launchSimulation(gridWidth: Int, gridHeight: Int, popRange: IntRange) {
        simConfig.gridWidth = gridWidth
        simConfig.gridHeight = gridHeight
        simConfig.populationRange = popRange
        simulationController.simConfig = simConfig
        simulationController.guiConfig = DefaultGUIConfig(simConfig)
        simulationController.startSimulation()
        view.replaceWith<SimulationView>()
    }

}
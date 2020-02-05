package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.SimulationController
import eu.bunburya.simugration.model.cell.CellGroup
import javafx.scene.control.Label
import tornadofx.*

class SimInfoView : View("Simulation Info") {

    val controller: SimulationController by inject()

    var cellCoordText: Label by singleAssign()
    var cellPopulationText: Label by singleAssign()
    var cellResourcesText: Label by singleAssign()
    var cellElevationText: Label by singleAssign()
    var cellDesirabilityText: Label by singleAssign()

    override val root = vbox {
        cellCoordText = label()
        cellPopulationText = label()
        cellResourcesText = label()
        cellElevationText = label()
        cellDesirabilityText = label()
    }

    fun updateCellLabels(coordText: String = "", populationText: String = "", resourcesText: String = "",
                         elevationText: String = "", desirabilityText: String = "") {
        cellCoordText.text = coordText
        cellPopulationText.text = populationText
        cellResourcesText.text = resourcesText
        cellElevationText.text = elevationText
        cellDesirabilityText.text = desirabilityText
    }

    fun updateCellData(selectedCells: CellGroup) {
        when (selectedCells.size) {
            0 -> updateCellLabels("No cell selected")
            1 -> {
                val entry = selectedCells.entries.toList()[0]
                val cell = entry.key
                val cellData = entry.value
                val (x, y) = cell.gridCoords

                updateCellLabels(
                    "Coordinates:\t($x, $y)",
                    "Population:\t${cellData.population}",
                    "Resources:\t${cellData.resources}",
                    "Elevation:\t${cellData.elevation}",
                    "Desirability:\t${cellData.desirability}"
                )
            }
            else -> {
                updateCellLabels(
                    "${selectedCells.size} cells selected",
                    "Mean population:\t${selectedCells.meanPopulation}",
                    "Mean resources:\t${selectedCells.meanResources}",
                    "Mean elevation:\t${selectedCells.meanElevation}",
                    "Mean desirability:\t${selectedCells.meanDesirability}"
                )
            }
        }
    }

}

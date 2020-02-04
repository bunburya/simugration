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
    var cellTerrainText: Label by singleAssign()

    override val root = vbox {
        cellCoordText = label()
        cellPopulationText = label()
        cellResourcesText = label()
        cellTerrainText = label()
    }

    fun updateCellLabels(coordText: String = "", populationText: String = "", resourcesText: String = "",
                         terrainText: String = "") {
        cellCoordText.text = coordText
        cellPopulationText.text = populationText
        cellResourcesText.text = resourcesText
        cellTerrainText.text = terrainText
    }

    fun updateCellData(selectedCells: CellGroup) {
        println("Updating cell data")
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
                    "Terrain:\t${cellData.terrain}"
                )
            }
            else -> {
                updateCellLabels(
                    "${selectedCells.size} cells selected",
                    "Mean population:\t${selectedCells.meanPopulation}",
                    "Mean resources:\t${selectedCells.meanResources}",
                    "Mean terrain:\t${selectedCells.meanTerrain}"
                )
            }
        }
    }

}

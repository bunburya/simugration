package eu.bunburya.simugration.controller

import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.SimConfig
import eu.bunburya.simugration.model.cell.Cell
import eu.bunburya.simugration.model.cell.CellGroup
import eu.bunburya.simugration.model.grid.Grid
import eu.bunburya.simugration.model.grid.HexGrid
import eu.bunburya.simugration.view.GridView
import eu.bunburya.simugration.view.SimInfoView
import eu.bunburya.simugration.view.SimulationView
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import tornadofx.Controller
import kotlin.properties.Delegates

const val STROKE_WIDTH = 1.0
const val SELECTED_STROKE_WIDTH = 3.0

class CellOutOfBoundsException(msg: String): Exception(msg)

class SimulationController: Controller() {
    private val simulationView:  SimulationView by inject()
    private val simInfoView: SimInfoView by inject()
    private val gridView: GridView by inject()

    var simConfig: SimConfig by Delegates.notNull()
    val cellPolygonMap = mutableMapOf<Cell, Polygon>()
    val selectedCells = CellGroup()

    lateinit var grid: Grid
    val cells get() = grid.keys

    fun getPopulation(cell: Cell) = getCellInfo(cell).population
    fun getResources(cell: Cell) = getCellInfo(cell).resources
    fun getTerrain(cell: Cell) = getCellInfo(cell).terrain

    fun startSimulation() {
        grid = HexGrid(simConfig)
        clear()
        drawPopulation()
        updateSimInfo()
        simulationView.openWindow()
    }

    // Functions for selecting cells

    fun selectCell(cell: Cell) {
        val cellInfo = grid[cell]
        if (cellInfo == null) {
            val (x,y) = cell.gridCoords
            throw CellOutOfBoundsException("Cell at ($x, $y) is out of bounds.")
        }
        selectedCells[cell] = cellInfo
        cellPolygonMap[cell]?.strokeWidth = SELECTED_STROKE_WIDTH
        updateSimInfo()
    }

    fun unselectCell(cell: Cell) {
        selectedCells.remove(cell)
        cellPolygonMap[cell]?.strokeWidth = STROKE_WIDTH
        updateSimInfo()
    }

    fun cellIsSelected(cell: Cell) = (cell in selectedCells)

    fun toggleCellSelected(cell: Cell) {
        if (cellIsSelected(cell)) unselectCell(cell)
        else selectCell(cell)
    }

    fun getCellInfo(cell: Cell): CellData {
        val cellData = grid[cell]
        if (cellData == null) throw CellOutOfBoundsException("Cell at ($(cell.x), $(cell.y)) out of bounds")
        else return cellData
    }

    fun clearCellSelection(updateView: Boolean = true) {
        selectedCells.clear()
        if (updateView) updateSimInfo()
    }

    fun selectSingleCell(cell: Cell) {
        clearCellSelection(false)
        selectCell(cell)
    }

    // Functions for drawing to gridView

    fun getCellColor(range: IntRange, value: Int): Color {
        val green = value.toDouble() / range.last
        val red = 1.0 - green
        return Color.color(red, green, 0.0)
    }

    fun drawPopulation() = gridView.draw(simConfig.populationRange, this::getPopulation)
    fun drawResources() = gridView.draw(simConfig.resourcesRange, this::getResources)
    fun drawTerrain() = gridView.draw(simConfig.terrainRange, this::getTerrain)

    fun clear() {
        cellPolygonMap.clear()
        clearCellSelection()
        gridView.clear()
    }

    // Functions for drawing to simInfoView

    fun updateSimInfo() {
        simInfoView.updateCellData(selectedCells)
    }

}
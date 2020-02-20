package eu.bunburya.simugration.controller

import eu.bunburya.simugration.model.Simulation
import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.config.SimConfig
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
import kotlin.math.min
import kotlin.properties.Delegates

const val STROKE_WIDTH = 1.0
const val SELECTED_STROKE_WIDTH = 3.0

class CellOutOfBoundsException(msg: String): Exception(msg)

enum class GridAspect {
    // The different views of the grid that you can select
    POPULATION,
    RESOURCES,
    ELEVATION
}

class SimulationController: Controller() {
    private val simulationView:  SimulationView by inject()
    private val simInfoView: SimInfoView by inject()
    private val gridView: GridView by inject()
    private var currentAspect: GridAspect = GridAspect.POPULATION

    var simConfig: SimConfig by Delegates.notNull()
    var simulation: Simulation by Delegates.notNull()
    val cellPolygonMap = mutableMapOf<Cell, Polygon>()
    val selectedCells = CellGroup()

    lateinit var grid: Grid
    val cells get() = grid.keys

    fun getPopulation(cell: Cell) = getCellInfo(cell).population
    fun getResources(cell: Cell) = getCellInfo(cell).resources
    fun getElevation(cell: Cell) = getCellInfo(cell).elevation

    fun startSimulation() {
        grid = HexGrid(simConfig)
        simulation = Simulation(simConfig, grid)
        clear()
        draw()
        updateSimInfo()
        simulationView.openWindow()
    }

    fun step() {
        simulation.step()
        draw()
        updateSimInfo()
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

    fun getCellColor(range: IntRange, value: Number): Color {
        val green = min(value.toDouble() / range.last, 1.0)
        val red = 1.0 - green
        return Color.color(red, green, 0.0)
    }
    fun getCellColor(range: ClosedFloatingPointRange<Double>, value: Number): Color {
        // Values above the range are clipped (so all values above a certain number will be displayed the same colour)
        val green = min(value.toDouble() / range.endInclusive, 1.0)
        val red = 1.0 - green
        return Color.color(red, green, 0.0)
    }

    fun drawPopulation() = gridView.draw(simConfig.populationRange, this::getPopulation)
    fun drawResources() = gridView.draw(simConfig.resourcesRange, this::getResources)
    fun drawElevation() = gridView.draw(simConfig.elevationRange, this::getElevation)

    fun draw(aspect: GridAspect = currentAspect) {
        if (aspect != currentAspect) {
            currentAspect = aspect
        }
        when (aspect) {
            GridAspect.POPULATION -> drawPopulation()
            GridAspect.RESOURCES -> drawResources()
            GridAspect.ELEVATION -> drawElevation()
        }
    }

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
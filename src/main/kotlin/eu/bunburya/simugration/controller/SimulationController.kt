package eu.bunburya.simugration.controller

import eu.bunburya.simugration.model.Simulation
import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.config.SimConfig
import eu.bunburya.simugration.model.cell.Cell
import eu.bunburya.simugration.model.cell.CellGroup
import eu.bunburya.simugration.model.grid.Grid
import eu.bunburya.simugration.model.grid.HexGrid
import eu.bunburya.simugration.roundTo
import eu.bunburya.simugration.view.GridView
import eu.bunburya.simugration.view.SimInfoView
import eu.bunburya.simugration.view.SimulationView
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import tornadofx.Controller
import tornadofx.observable
import tornadofx.singleAssign
import kotlin.math.min
import kotlin.properties.Delegates

const val STROKE_WIDTH = 1.0
const val SELECTED_STROKE_WIDTH = 3.0

class CellOutOfBoundsException(msg: String): Exception(msg)

enum class GridAspect {
    // The different views of the grid that you can select
    POPULATION,
    RESOURCES,
    ELEVATION,
    DESIRABILITY
}

fun getStringProperty(bean: Any, name: String, initialValue: Any): SimpleStringProperty {
    return SimpleStringProperty(bean, name, initialValue.toString())
}

class RowData(
    val coordsProperty: SimpleStringProperty,
    val populationProperty: SimpleStringProperty,
    val resourcesProperty: SimpleStringProperty,
    val elevationProperty: SimpleStringProperty,
    val desirabilityProperty: SimpleStringProperty
)

fun CellData.rowData(): RowData {
    return RowData(
        getStringProperty(
            this, "Coords",
            "(${this.cell.gridCoords.x}, ${this.cell.gridCoords.y})"
        ),
        getStringProperty(this, "Population", this.population),
        getStringProperty(this, "Resources", this.resources),
        getStringProperty(this, "Elevation", this.elevation),
        getStringProperty(this, "Desirability", this.desirability)
    )
}

fun CellGroup.rowDataList(): List<RowData> {
    if (this.isEmpty()) return emptyList()
    else return this.values.map { it.rowData() } + listOf(
        RowData(
            getStringProperty(this, "Coords", "Average"),
            getStringProperty(this, "Population", this.meanPopulation),
            getStringProperty(this, "Resources", this.meanResources),
            getStringProperty(this, "Elevation", this.meanElevation),
            getStringProperty(
                this,
                "Desirability",
                this.meanDesirability
            )
        ),
        RowData(
            getStringProperty(this, "Coords", "Total"),
            getStringProperty(this, "Population", this.totalPopulation),
            getStringProperty(this, "Resources", this.totalResources),
            getStringProperty(this, "Elevation", this.totalElevation),
            getStringProperty(
                this,
                "Desirability",
                this.totalDesirability
            )
        )
    )
}

class SimulationController: Controller() {
    private val simulationView: SimulationView by inject()
    private val simInfoView: SimInfoView by inject()
    private val gridView: GridView by inject()
    private var currentAspect: GridAspect = GridAspect.POPULATION

    var simConfig: SimConfig by Delegates.notNull()
    var guiConfig: GUIConfig by singleAssign()
    var simulation: Simulation by Delegates.notNull()
    val cellPolygonMap = mutableMapOf<Cell, Polygon>()
    val selectedCells = CellGroup.newCellGroup(orderMatters = true)

    lateinit var grid: Grid
    val cells get() = grid.keys

    fun getPopulation(cell: Cell): Int = getCellInfo(cell).population
    fun getResources(cell: Cell): Int = getCellInfo(cell).resources
    fun getElevation(cell: Cell): Double = getCellInfo(cell).elevation.roundTo(guiConfig.decimalPrecision)
    fun getDesirability(cell: Cell): Double = getCellInfo(cell).desirability.roundTo(guiConfig.decimalPrecision)

    fun startSimulation() {
        grid = HexGrid(simConfig)
        simulation = Simulation(simConfig, grid)
        clear()
        draw()
        updateAllInfo()
        //simulationView.openWindow()
    }

    fun step() {
        simulation.step()
        draw()
        updateAllInfo()
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
        updateCellInfo()
    }

    fun unselectCell(cell: Cell) {
        selectedCells.remove(cell)
        cellPolygonMap[cell]?.strokeWidth = STROKE_WIDTH
        updateCellInfo()
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
        if (updateView) updateCellInfo()
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
        val effectiveVal = min(value.toDouble() / range.endInclusive, 1.0)
        val green = effectiveVal
        val red = 1.0 - green
        return Color.color(red, green, 0.0)
    }

    fun drawPopulation() = gridView.draw(guiConfig.drawablePopulationRange, this::getPopulation)
    fun drawResources() = gridView.draw(guiConfig.drawableResourcesRange, this::getResources)
    fun drawElevation() = gridView.draw(guiConfig.drawableElevationRange, this::getElevation)
    fun drawDesirability() = gridView.draw(guiConfig.drawableDesirabilityRange, this::getDesirability)

    fun draw(aspect: GridAspect = currentAspect) {
        if (aspect != currentAspect) {
            currentAspect = aspect
        }
        when (aspect) {
            GridAspect.POPULATION -> drawPopulation()
            GridAspect.RESOURCES -> drawResources()
            GridAspect.ELEVATION -> drawElevation()
            GridAspect.DESIRABILITY -> drawDesirability()
        }
    }

    fun clear() {
        cellPolygonMap.clear()
        clearCellSelection()
        gridView.clear()
    }

    // Functions for drawing to simInfoView

    fun updateAllInfo() {
        updateCellInfo()
        updateSimInfo()
    }

    fun updateCellInfo() {
        simInfoView.cellInfoTable.items = selectedCells.rowDataList().observable()
    }
    fun updateSimInfo() {
        simInfoView.apply{
            simStepLabel.text = simulation.step.toString()
            simMeanPopulationLabel.text = grid.meanPopulation.roundTo(guiConfig.decimalPrecision).toString()
            simTotalPopulationLabel.text = grid.totalPopulation.toString()
            simMeanResourcesLabel.text = grid.meanResources.roundTo(guiConfig.decimalPrecision).toString()
            simTotalResourcesLabel.text = grid.totalResources.toString()
            simMeanElevationLabel.text = grid.meanElevation.roundTo(guiConfig.decimalPrecision).toString()
            simTotalElevationLabel.text = grid.totalElevation.roundTo(guiConfig.decimalPrecision).toString()
            simMeanDesirabilityLabel.text = grid.meanDesirability.roundTo(guiConfig.decimalPrecision).toString()
            simTotalDesirabilityLabel.text = grid.totalDesirability.roundTo(guiConfig.decimalPrecision).toString()
        }
    }

}
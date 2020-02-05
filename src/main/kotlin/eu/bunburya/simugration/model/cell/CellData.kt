package eu.bunburya.simugration.model.cell

import eu.bunburya.simugration.model.config.InitialState
import eu.bunburya.simugration.model.config.SimRules
import eu.bunburya.simugration.model.grid.Grid
import kotlin.properties.Delegates

class CellData(val grid: Grid, val cell: Cell) {

    val neighbours: Array<CellData?> by lazy {
        Array<CellData?>(6) { i -> grid[cell.neighbours[i]] }
    }
    var population: Int by Delegates.notNull()
    var resources: Int by Delegates.notNull()
    var elevation: Double by Delegates.notNull()
    lateinit var desirabilityFunc: (CellData) -> Double
    val desirability: Double get() = desirabilityFunc(this)

    fun initialize(initialState: InitialState, simRules: SimRules) {
        population = initialState.getPopulation(this)
        resources = initialState.getResources(this)
        elevation = initialState.getElevation(this)
        desirabilityFunc = simRules::desirability
    }

}
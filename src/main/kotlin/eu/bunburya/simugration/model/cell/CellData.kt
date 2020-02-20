package eu.bunburya.simugration.model.cell

import eu.bunburya.simugration.model.config.InitialState
import eu.bunburya.simugration.model.config.SimRules
import eu.bunburya.simugration.model.grid.Grid
import kotlin.properties.Delegates

class CellData(val grid: Grid, val cell: Cell) {

    val neighbourGroup: CellGroup by lazy { grid.subGroup(*cell.neighbours) }
    var population: Int by Delegates.notNull()
    var resources: Int by Delegates.notNull()
    var elevation: Double by Delegates.notNull()
    var stickiness: Double by Delegates.notNull()
    lateinit var migratablePopFunc: (CellData) -> Int
    val migratablePop: Int get() = migratablePopFunc(this)
    val migratablePopPerNeighbour: Int get() = migratablePop / cell.neighbours.size
    lateinit var desirabilityFunc: (CellData) -> Double
    val desirability: Double get() = desirabilityFunc(this)

    fun initialize(initialState: InitialState, simRules: SimRules) {
        population = initialState.getPopulation(this)
        resources = initialState.getResources(this)
        elevation = initialState.getElevation(this)
        stickiness = simRules.stickiness
        desirabilityFunc = simRules::desirabilityFunc
        migratablePopFunc = simRules::migratablePopFunc
    }

}
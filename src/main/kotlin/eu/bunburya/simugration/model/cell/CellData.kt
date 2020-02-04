package eu.bunburya.simugration.model.cell

import eu.bunburya.simugration.model.InitialState
import eu.bunburya.simugration.model.grid.Grid

class CellData(val grid: Grid, val cell: Cell, initState: InitialState) {

    val neighbours: Array<CellData?> by lazy {
        Array<CellData?>(6) { i -> grid[cell.neighbours[i]] }
    }
    var population = initState.getPopulation(this)
    var resources = initState.getResources(this)
    val terrain = initState.getTerrain(this)

}
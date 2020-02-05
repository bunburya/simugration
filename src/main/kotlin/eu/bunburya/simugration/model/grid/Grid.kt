package eu.bunburya.simugration.model.grid

import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.cell.Cell
import eu.bunburya.simugration.model.cell.CellGroup
import eu.bunburya.simugration.model.config.SimConfig

abstract class Grid(val simConfig: SimConfig, cellMap: MutableMap<Cell, CellData> = mutableMapOf()) : CellGroup(cellMap) {

    open fun initializeCellData() {
        for (cellData in values) {
            cellData.initialize(simConfig.initialState, simConfig.simRules)
        }
    }

}
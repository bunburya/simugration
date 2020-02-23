package eu.bunburya.simugration.model.grid

import eu.bunburya.hexagons.MapBuilder
import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.config.SimConfig
import eu.bunburya.simugration.model.cell.Cell
import eu.bunburya.simugration.model.cell.HexCell

class HexGrid(simConfig: SimConfig): Grid(simConfig) {

    // TODO:  Fix MapBuilder.rectangle so we don't need to subtract 1 from gridWidth here
    private val cells = MapBuilder.rectangle(simConfig.gridHeight, simConfig.gridWidth-1, "qr").map {
        hex -> HexCell(hex)
    }
    init {
        for (c in cells) {
            // NOTE:  If initial value of a cell's properties depends on its neighbours, then we must completely
            // initialise innerMap before initialising a cell.
            this[c] = CellData(this, c)
        }
        initializeCellData()
    }
}
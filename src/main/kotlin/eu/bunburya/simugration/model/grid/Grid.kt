package eu.bunburya.simugration.model.grid

import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.cell.Cell
import eu.bunburya.simugration.model.cell.CellGroup

abstract class Grid(cellMap: MutableMap<Cell, CellData> = mutableMapOf()) : CellGroup(cellMap)
package eu.bunburya.simugration.model.cell

import eu.bunburya.simugration.BadCellError

open class CellGroup(private val cellMap: MutableMap<Cell, CellData> = mutableMapOf()): MutableMap<Cell, CellData> by cellMap {

    val meanPopulation: Double get() = values.sumBy { it.population } / cellMap.size.toDouble()
    val meanResources: Double get() = values.sumBy { it.resources } / cellMap.size.toDouble()
    val meanElevation: Double get() = values.sumByDouble { it.elevation } / cellMap.size
    val meanDesirability: Double get() = values.sumByDouble { it.desirability } / cellMap.size

    fun subGroup(vararg cells: Cell): CellGroup {
        val newCellMap: MutableMap<Cell, CellData> = mutableMapOf()
        var cellData: CellData?
        for (c in cells) {
            cellData = this[c]
            if (cellData != null) newCellMap[c] = cellData
        }
        return CellGroup(newCellMap)
    }

}
package eu.bunburya.simugration.model.cell

import eu.bunburya.simugration.BadCellError

open class CellGroup(private val cellMap: MutableMap<Cell, CellData>, val orderMatters: Boolean = false): MutableMap<Cell, CellData> by cellMap {

    companion object {
        fun newCellGroup(orderMatters: Boolean = false, contents: Map<Cell, CellData> = emptyMap()): CellGroup {
            val cellMap: MutableMap<Cell, CellData>
            if (orderMatters) cellMap = linkedMapOf()
            else cellMap = mutableMapOf()
            for ((cell, cellData) in contents) {
                cellMap[cell] = cellData
            }
            return CellGroup(cellMap, orderMatters)
        }
    }

    val meanPopulation: Double get() = values.sumBy { it.population } / cellMap.size.toDouble()
    val meanResources: Double get() = values.sumBy { it.resources } / cellMap.size.toDouble()
    val meanElevation: Double get() = values.sumByDouble { it.elevation } / cellMap.size
    val meanDesirability: Double get() = values.sumByDouble { it.desirability } / cellMap.size

    val totalPopulation: Int get() = values.sumBy { it.population }
    val totalResources: Int get() = values.sumBy { it.resources }
    val totalElevation: Double get() = values.sumByDouble { it.elevation }
    val totalDesirability: Double get() = values.sumByDouble { it.desirability }

    fun subGroup(vararg cells: Cell, orderMatters: Boolean = false): CellGroup {
        val contents: MutableMap<Cell, CellData?>
        if (orderMatters) contents = linkedMapOf()
        else contents = mutableMapOf()
        cells.map { it to this[it] }.toMap(contents)
        return newCellGroup(orderMatters, contents.filterValues { it != null } as Map<Cell, CellData>)
    }

}
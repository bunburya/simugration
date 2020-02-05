package eu.bunburya.simugration.model.cell

open class CellGroup(private val cellMap: MutableMap<Cell, CellData> = mutableMapOf()): MutableMap<Cell, CellData> by cellMap {

    val meanPopulation: Double get() = values.sumBy { it.population } / cellMap.size.toDouble()
    val meanResources: Double get() = values.sumBy { it.resources } / cellMap.size.toDouble()
    val meanElevation: Double get() = values.sumByDouble { it.elevation } / cellMap.size
    val meanDesirability: Double get() = values.sumByDouble { it.desirability } / cellMap.size

}
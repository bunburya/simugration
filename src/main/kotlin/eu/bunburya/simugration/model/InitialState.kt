package eu.bunburya.simugration.model

import eu.bunburya.simugration.model.cell.CellData
import kotlin.math.roundToInt
import kotlin.math.sin

interface InitialState {
    val simConfig: SimConfig
    fun getPopulation(cellData: CellData): Int
    fun getResources(cellData: CellData): Int
    fun getTerrain(cellData: CellData): Int
}

class RandomInitialState(override val simConfig: SimConfig) : InitialState {

    override fun getPopulation(cellData: CellData): Int {
        return simConfig.populationRange.random()
    }

    override fun getResources(cellData: CellData): Int {
        return simConfig.resourcesRange.random()
    }

    override fun getTerrain(cellData: CellData): Int {
        return simConfig.terrainRange.random()
    }

}

class SinePopInitialState(override val simConfig: SimConfig) : InitialState {

    override fun getPopulation(cellData: CellData): Int {
        //return POP_RANGE.random()
        val (col, row) = cellData.cell.gridCoords
        return (((((sin(col.toDouble()) + 1) / 2) * simConfig.populationRange.last) +
                ((sin(row.toDouble()) + 1) / 2) * simConfig.populationRange.last) / 2).roundToInt()
    }

    override fun getResources(cellData: CellData): Int {
        return simConfig.resourcesRange.random()
    }

    override fun getTerrain(cellData: CellData): Int {
        return simConfig.terrainRange.random()
    }

}
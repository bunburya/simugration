package eu.bunburya.simugration.model.config

import eu.bunburya.simugration.model.cell.CellData
import kotlin.math.roundToInt
import kotlin.math.sin

interface InitialState {
    val simConfig: SimConfig
    fun getPopulation(cellData: CellData): Int
    fun getResources(cellData: CellData): Int
    fun getElevation(cellData: CellData): Double
}

class RandomInitialState(override val simConfig: SimConfig): InitialState {

    override fun getPopulation(cellData: CellData): Int {
        return simConfig.populationRange.random()
    }

    override fun getResources(cellData: CellData): Int {
        return simConfig.resourcesRange.random()
    }

    override fun getElevation(cellData: CellData): Double {
        return simConfig.elevationRange.random() as Double
    }

}

class AveragedRandomElevation(override val simConfig: SimConfig): InitialState {

    private val randomValues: Array<IntArray> by lazy {
        Array(simConfig.gridHeight) {
            IntArray(simConfig.gridWidth) {
                simConfig.elevationRange.random()
            }
        }
    }

    private fun getRandomValue(cellData: CellData): Int {
        var (x, y) = cellData.cell.gridCoords
        if (x % 2 == 1) y--
        return randomValues[y][x]
    }

    override fun getPopulation(cellData: CellData): Int {
        return simConfig.populationRange.random()
    }

    override fun getResources(cellData: CellData): Int {
        return (simConfig.resourcesRange.random() * (getElevation(cellData) / simConfig.resourcesRange.last.toDouble())).roundToInt()
    }

    override fun getElevation(cellData: CellData): Double {
        val cellsToAverage: List<CellData> = cellData.neighbours.filterNotNull() + cellData
        val randomValuesToAverage: List<Int> = cellsToAverage.map { getRandomValue(it) }
        return randomValuesToAverage.average()
    }
}

class SinePopInitialState(override val simConfig: SimConfig): InitialState {

    override fun getPopulation(cellData: CellData): Int {
        //return POP_RANGE.random()
        val (col, row) = cellData.cell.gridCoords
        return (((((sin(col.toDouble()) + 1) / 2) * simConfig.populationRange.last) +
                ((sin(row.toDouble()) + 1) / 2) * simConfig.populationRange.last) / 2).roundToInt()
    }

    override fun getResources(cellData: CellData): Int {
        return simConfig.resourcesRange.random()
    }

    override fun getElevation(cellData: CellData): Double {
        return simConfig.elevationRange.random() as Double
    }

}
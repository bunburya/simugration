package eu.bunburya.simugration.model.config

import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.normalize
import eu.bunburya.simugration.random
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

    private val randomValues: Array<DoubleArray> by lazy {
        Array(simConfig.gridHeight) {
            DoubleArray(simConfig.gridWidth) {
                simConfig.elevationRange.random()
            }
        }
    }

    private fun getRandomValue(cellData: CellData): Double {
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
        val randomValuesToAverage: List<Double> = cellsToAverage.map { getRandomValue(it) }
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

class PerlinInitialState(override val simConfig: SimConfig): InitialState {

    val perlin = ImprovedPerlinNoise2D()

    val octaves = 6
    val lacunarity = 2.0
    val persistence = 0.5
    val scale = 0.5

    override fun getElevation(cellData: CellData): Double {
        val noise = perlin.octaveNoise(
            cellData.cell.center.first * scale,
            cellData.cell.center.second * scale,
            octaves,
            lacunarity,
            persistence
        )
        val noiseRange = perlin.getNoiseRange(octaves, persistence, 0.1)
        return normalize(noise,  noiseRange, simConfig.elevationRange)
    }

    override fun getPopulation(cellData: CellData): Int {
        return simConfig.populationRange.random()
    }

    override fun getResources(cellData: CellData): Int {
        return (simConfig.resourcesRange.random() * (getElevation(cellData) / simConfig.resourcesRange.last.toDouble())).roundToInt()
    }
}
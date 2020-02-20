package eu.bunburya.simugration.model.config

import eu.bunburya.simugration.model.cell.CellData
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

interface SimRules {
    fun desirabilityFunc(cellData: CellData): Double
    fun migratablePopFunc(cellData: CellData): Int
    fun migrationFunc(cellData1: CellData, cellData2: CellData):Int
    val stickiness: Double
}

class TestSimRules(val config: SimConfig): SimRules {
    override fun desirabilityFunc(cellData: CellData): Double {
        var popFactor = cellData.population
        if (popFactor > config.populationRange.last) popFactor -= (popFactor - (config.populationRange.last / 2)) * 2
        val elevFactor = config.elevationRange.endInclusive - cellData.elevation
        val resFactor =  cellData.resources
        val neighbourPopFactor = cellData.neighbourGroup.meanPopulation
        val neighbourResFactor = cellData.neighbourGroup.meanResources
        return max(0.0, popFactor + elevFactor + (resFactor*6) + (neighbourPopFactor/2) + neighbourResFactor)
    }

    override fun migratablePopFunc(cellData: CellData): Int {
        return (cellData.population * (1 - cellData.stickiness)).roundToInt()
    }

    override fun migrationFunc(cellData1: CellData, cellData2: CellData): Int {

        val source: CellData
        val dest: CellData
        val direction: Int
        if (cellData1.desirability > cellData2.desirability) {
            source = cellData2
            dest = cellData1
            direction = -1
        } else if (cellData1.desirability < cellData2.desirability) {
            source = cellData1
            dest = cellData2
            direction = 1
        } else {
            // desirability is equal
            return 0
        }
        return (source.migratablePopPerNeighbour * (1 - (source.desirability / dest.desirability))).roundToInt() * direction
    }

    override val stickiness = 0.6
}
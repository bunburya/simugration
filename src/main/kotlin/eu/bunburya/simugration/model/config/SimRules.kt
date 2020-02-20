package eu.bunburya.simugration.model.config

import eu.bunburya.simugration.model.cell.CellData
import kotlin.math.abs
import kotlin.math.roundToInt

interface SimRules {
    fun desirability(cellData: CellData): Double
    fun migratablePop(cellData: CellData): Int
    val stickiness: Double
}

class TestSimRules(val config: SimConfig): SimRules {
    override fun desirability(cellData: CellData): Double {
        var popFactor = cellData.population
        if (popFactor > config.populationRange.last) popFactor -= (popFactor - config.populationRange.last) * 2
        val elevFactor = config.elevationRange.endInclusive - cellData.elevation
        val resFactor =  cellData.resources
        return popFactor + elevFactor + resFactor*10
    }

    override fun migratablePop(cellData: CellData): Int {
        return cellData.population * (1 - cellData.stickiness).roundToInt()
    }

    override val stickiness = 0.2
}
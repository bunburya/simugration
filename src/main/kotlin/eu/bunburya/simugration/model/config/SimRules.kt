package eu.bunburya.simugration.model.config

import eu.bunburya.simugration.model.cell.CellData
import kotlin.math.abs

interface SimRules {
    fun desirability(cellData: CellData): Double
}

class TestSimRules(val config: SimConfig): SimRules {
    override fun desirability(cellData: CellData): Double {
        var popFactor = cellData.population
        if (popFactor > config.populationRange.last) popFactor -= (popFactor - config.populationRange.last) * 2
        val elevFactor = config.elevationRange.last - cellData.elevation
        val resFactor =  cellData.resources
        return popFactor + elevFactor + resFactor*10
    }
}
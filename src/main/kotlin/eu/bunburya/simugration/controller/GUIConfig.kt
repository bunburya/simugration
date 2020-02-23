package eu.bunburya.simugration.controller

import eu.bunburya.simugration.model.config.SimConfig
import eu.bunburya.simugration.toDoubleRange
import eu.bunburya.simugration.times

interface GUIConfig {
    val drawablePopulationRange: ClosedFloatingPointRange<Double>
    val drawableResourcesRange: ClosedFloatingPointRange<Double>
    val drawableElevationRange: ClosedFloatingPointRange<Double>
    val drawableDesirabilityRange: ClosedFloatingPointRange<Double>

    val decimalPrecision: Int
}

class DefaultGUIConfig(simConfig: SimConfig): GUIConfig {
    override val drawablePopulationRange = simConfig.populationRange.toDoubleRange() * 1.5
    override val drawableResourcesRange = simConfig.resourcesRange.toDoubleRange()
    override val drawableElevationRange: ClosedFloatingPointRange<Double> = simConfig.elevationRange
    override val drawableDesirabilityRange: ClosedFloatingPointRange<Double> = simConfig.populationRange.toDoubleRange() * 10.0

    override val decimalPrecision = 2
}
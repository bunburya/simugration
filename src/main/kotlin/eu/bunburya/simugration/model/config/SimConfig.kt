package eu.bunburya.simugration.model.config

class SimConfig {

    val configMap = mutableMapOf<String, Any?>(
        "resourcesRange" to 0 .. 10000,
        "elevationRange" to 0.0 .. 10000.0
    )
    var gridWidth: Int by configMap
    var gridHeight: Int by configMap
    var populationRange: IntRange by configMap
    var resourcesRange: IntRange by configMap
    var elevationRange: ClosedFloatingPointRange<Double> by configMap

    //val initialState: InitialState = AveragedRandomElevation(this)
    //val initialState: InitialState = RandomInitialState(this)
    val initialState: InitialState = PerlinInitialState(this)
    val simRules: SimRules = TestSimRules(this)

}
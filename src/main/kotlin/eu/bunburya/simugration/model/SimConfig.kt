package eu.bunburya.simugration.model

class SimConfig {

    val configMap = mutableMapOf<String, Any?>(
        "resourcesRange" to 0..10000,
        "terrainRange" to 0..10000
    )
    var gridWidth: Int by configMap
    var gridHeight: Int by configMap
    var populationRange: IntRange by configMap
    var resourcesRange: IntRange by configMap
    var terrainRange: IntRange by configMap

    val initialState: InitialState = RandomInitialState(this)

}
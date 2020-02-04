package eu.bunburya.simugration.model.grid

import eu.bunburya.hexagons.MapBuilder
import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.SimConfig
import eu.bunburya.simugration.model.cell.Cell
import eu.bunburya.simugration.model.cell.HexCell

class HexGrid(config: SimConfig): Grid() {

    private val _entries = MapBuilder.rectangle(config.gridHeight, config.gridWidth, "qr").map {
        hex ->
        HexCell(hex)
    }
    private val innerMap: MutableMap<Cell, CellData>
    init {
        val pairArray: Array<Pair<Cell, CellData>> = Array(_entries.size) { i ->
            val cell = _entries[i]
            Pair(cell, CellData(this, cell, config.initialState))
        }
        innerMap = mutableMapOf(*pairArray)
    }
    override val entries = innerMap.entries
    override val keys = innerMap.keys
    override val size = innerMap.size
    override val values = innerMap.values
    override fun containsKey(key: Cell): Boolean = innerMap.containsKey(key)
    override fun containsValue(value: CellData): Boolean = innerMap.containsValue(value)
    override fun get(key: Cell): CellData? = innerMap[key]
    override fun isEmpty(): Boolean = innerMap.isEmpty()
}
package eu.bunburya.simugration.model.cell

import eu.bunburya.hexagons.*
import eu.bunburya.simugration.model.grid.GridCoordinates

data class HexCell(val hex: Hex, val offset: Offset = Offset.EVEN): Cell {

    private val layout = Layout(LAYOUT_FLAT, Point(10.0, 10.0), Point(0.0, 0.0))
    private val offsetCoords = OffsetCoord.qOffsetFromCube(offset, hex)
    override val neighbours: Array<Cell> by lazy { Array<Cell>(6) { i -> HexCell(hex.neighbour(i)) } }
    override val gridCoords =
        GridCoordinates(offsetCoords.col, offsetCoords.row)
    override val points = Array<Double>(12) { i -> layout.polygonCorners(hex).flatten()[i] }

    override val center: Pair<Double, Double> by lazy {
        val point = layout.hexToPixel(hex)
        Pair(point.x, point.y)
    }

}
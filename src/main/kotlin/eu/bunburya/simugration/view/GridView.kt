package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.STROKE_WIDTH
import eu.bunburya.simugration.controller.SimulationController
import eu.bunburya.simugration.model.cell.Cell
import javafx.event.EventHandler
import javafx.scene.paint.Color
import tornadofx.*

class GridView: View("HexGrid View") {
    private val controller: SimulationController by inject()
    override val root = group()

    fun clear() = root.children.clear()

    fun draw(range: IntRange, valueFunc: (Cell) -> Int) {
        for (cell in controller.cells) {
            val p = controller.cellPolygonMap[cell]
            if (p == null) {
                root.polygon(*cell.points) {
                    stroke = Color.BLACK
                    strokeWidth = STROKE_WIDTH
                    fill = controller.getCellColor(range, valueFunc(cell))
                    controller.cellPolygonMap[cell] = this
                    this.onMouseClicked = EventHandler {
                        controller.toggleCellSelected(cell)
                    }
                }
            } else {
                p.fill = controller.getCellColor(range, valueFunc(cell))
            }
        }
    }

}

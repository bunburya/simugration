package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.STROKE_WIDTH
import eu.bunburya.simugration.controller.SimulationController
import eu.bunburya.simugration.model.cell.Cell
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.paint.Color
import tornadofx.*

class GridView: View("Grid View") {
    private val controller: SimulationController by inject()
    private var gridGroup: Group by singleAssign()
    override val root = scrollpane {
        gridGroup = group()
    }

    fun clear() = gridGroup.children.clear()

    fun draw(range: IntRange, valueFunc: (Cell) -> Number) {
        val floatRange = range.first.toDouble()..range.last.toDouble()
        return draw(floatRange, valueFunc)
    }
    fun draw(range: ClosedFloatingPointRange<Double>, valueFunc: (Cell) -> Number) {
        for (cell in controller.cells) {
            val p = controller.cellPolygonMap[cell]
            if (p == null) {
                gridGroup.polygon(*cell.points) {
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

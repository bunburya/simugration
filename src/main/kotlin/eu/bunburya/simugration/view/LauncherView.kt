package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.LauncherController
import javafx.scene.control.Spinner
import javafx.scene.control.TextField
import tornadofx.*

class LauncherView : View("Launch new simulation") {

    val controller: LauncherController by inject()
    var heightField: Spinner<Int> by singleAssign()
    var widthField: Spinner<Int> by singleAssign()
    var popRangeMinField: TextField by singleAssign()
    var popRangeMaxField: TextField by singleAssign()

    override val root = form {
        fieldset("Grid dimensions") {
            field("Grid width") {
                widthField = spinner(1, 80, 60)
            }
            field("Grid height") {
                    heightField = spinner(1, 45, 30)
            }
        }
        fieldset("Initial state") {
            field("Initial population range") {
                popRangeMinField = textfield("0") {
                    filterInput { it.controlNewText.isInt() }
                }
                popRangeMaxField = textfield("10000") {
                    filterInput { it.controlNewText.isInt() }
                }
            }
        }
        button("Launch") {
            action {
                controller.launchSimulation(
                    gridWidth = widthField.value,
                    gridHeight = heightField.value,
                    popRange = popRangeMinField.text.toInt()..popRangeMaxField.text.toInt()
                )
            }
        }
    }
}

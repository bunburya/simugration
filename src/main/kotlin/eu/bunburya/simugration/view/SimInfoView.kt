package eu.bunburya.simugration.view

import eu.bunburya.simugration.controller.RowData
import eu.bunburya.simugration.controller.SimulationController
import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.cell.CellGroup
import javafx.beans.property.*
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import tornadofx.*

class SimInfoView : View("Simulation Info") {

    val controller: SimulationController by inject()

    var cellInfoTable: TableView<RowData> by singleAssign()
    var simInfoTable: GridPane by singleAssign()

    var simStepLabel: Label by singleAssign()
    var simMeanPopulationLabel: Label by singleAssign()
    var simTotalPopulationLabel: Label by singleAssign()
    var simMeanResourcesLabel: Label by singleAssign()
    var simTotalResourcesLabel: Label by singleAssign()
    var simMeanElevationLabel: Label by singleAssign()
    var simTotalElevationLabel: Label by singleAssign()
    var simMeanDesirabilityLabel: Label by singleAssign()
    var simTotalDesirabilityLabel: Label by singleAssign()

    override val root = hbox {
        cellInfoTable = tableview<RowData> {
            column("Coords", RowData::coordsProperty)
            column("Population", RowData::populationProperty)
            column("Resources", RowData::resourcesProperty)
            column("Elevation", RowData::elevationProperty)
            column("Desirability", RowData::desirabilityProperty)
            prefHeight = 150.0 // TODO:  Find a way to smartly set this, possibly by using CSS,
            hgrow = Priority.ALWAYS
        }
        simInfoTable = gridpane {
            row {
                label("Step:")
                simStepLabel = label()
            }
            row {
                label()
                label("Average").style { fontWeight = FontWeight.BOLD }
                label("Total").style { fontWeight = FontWeight.BOLD }
            }
            row {
                label("Population:")
                    .gridpaneConstraints { marginRight = 10.0 }
                    .style { fontWeight = FontWeight.BOLD }
                simMeanPopulationLabel = label().gridpaneConstraints { marginRight = 10.0 }
                simTotalPopulationLabel = label()
            }
            row {
                label("Resources:")
                    .gridpaneConstraints { marginRight = 10.0 }
                    .style { fontWeight = FontWeight.BOLD }
                simMeanResourcesLabel = label().gridpaneConstraints { marginRight = 10.0 }
                simTotalResourcesLabel = label()
            }
            row {
                label("Elevation:")
                    .gridpaneConstraints { marginRight = 10.0 }
                    .style { fontWeight = FontWeight.BOLD }
                simMeanElevationLabel = label().gridpaneConstraints { marginRight = 10.0 }
                simTotalElevationLabel = label()
            }
            row {
                label("Desirability:")
                    .gridpaneConstraints { marginRight = 10.0 }
                    .style { fontWeight = FontWeight.BOLD }
                simMeanDesirabilityLabel = label().gridpaneConstraints { marginRight = 10.0 }
                simTotalDesirabilityLabel = label()
            }
        }
    }



}

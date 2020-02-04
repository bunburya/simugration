package eu.bunburya.simugration.model.cell

import eu.bunburya.simugration.model.grid.GridCoordinates

interface Cell {

    val neighbours: Array<Cell>
    val gridCoords: GridCoordinates
    val points: Array<Double>

}
package eu.bunburya.simugration.model

import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.config.SimConfig
import eu.bunburya.simugration.model.grid.Grid
import kotlin.math.roundToInt

class Simulation(val simConfig: SimConfig, val grid: Grid) {

    fun step() {
        val migrationRecord = calculateAllMigrations()
        return applyMigrations(migrationRecord)

    }

    fun calculateAllMigrations(): MutableMap<Pair<CellData, CellData>, Int> {
        val migrationRecord = mutableMapOf<Pair<CellData, CellData>, Int>()
        val processedNeighbours = mutableMapOf<CellData, MutableList<CellData>>()
        var cellData: CellData
        var processed: MutableList<CellData>
        for (entry in grid) {
            cellData = entry.value
            if (cellData !in processedNeighbours.keys) processedNeighbours[cellData] = mutableListOf()
            for (n in cellData.neighbours.filterNotNull()) {
                processed = processedNeighbours[cellData]!!
                if (n !in processed) {
                    calculateMigration(cellData, n!!, migrationRecord)
                    processedNeighbours[cellData]?.add(n)
                }
            }
        }
        return migrationRecord
    }

    fun calculateMigration(cellData1: CellData, cellData2: CellData, migrationRecord: MutableMap<Pair<CellData, CellData>, Int>) {

        var source: CellData
        var dest: CellData
        if (cellData1.desirability > cellData2.desirability) {
            source = cellData2
            dest = cellData1
        } else if (cellData1.desirability < cellData2.desirability) {
            source = cellData1
            dest = cellData2
        } else {
            // desirability is equal
            return
        }
        migrationRecord[Pair(source, dest)] = (source.migratablePopPerNeighbour * (1 - (dest.desirability / source.desirability))).roundToInt()
    }

    fun applyMigrations(migrationRecord: MutableMap<Pair<CellData, CellData>, Int>) {
        var source: CellData
        var dest: CellData
        var migration: Int
        for (entry in migrationRecord) {
            source = entry.key.first
            dest = entry.key.second
            migration = entry.value
            source.population -= migration
            dest.population += migration
        }
    }

}
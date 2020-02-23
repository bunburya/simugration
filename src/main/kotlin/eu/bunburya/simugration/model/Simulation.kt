package eu.bunburya.simugration.model

import eu.bunburya.simugration.model.cell.CellData
import eu.bunburya.simugration.model.config.SimConfig
import eu.bunburya.simugration.model.grid.Grid

class Simulation(val simConfig: SimConfig, val grid: Grid) {

    var step = 0

    /**
     * calculateMigration is a function to calculate the migration between the cells represented by cellData1 and
     * cellData2, for one step of the simulation.  It should return an Int representing the number of population to
     * migrate.  A positive number means that the migration flows from the first provided cell to the second; a negative
     * number means the migration flows from the second provided cell to the first.
     */
    val calculateMigration = simConfig.simRules::migrationFunc

    fun step() {
        val migrationRecord = calculateAllMigrations()
        //println(migrationRecord)
        applyMigrations(migrationRecord)
        step++
    }

    fun calculateAllMigrations(): MutableMap<Pair<CellData, CellData>, Int> {
        val migrationRecord = mutableMapOf<Pair<CellData, CellData>, Int>()
        val processedNeighbours = mutableMapOf<CellData, MutableList<CellData>>()
        var cellData: CellData
        var processed: MutableList<CellData>
        var migration: Int
        for (entry in grid) {
            cellData = entry.value
            if (cellData !in processedNeighbours.keys) processedNeighbours[cellData] = mutableListOf()
            for (n in cellData.neighbourGroup.values) {
                processed = processedNeighbours[cellData]!!
                if (n !in processed) {
                    migration = calculateMigration(cellData, n)
                    if (migration > 0) migrationRecord[Pair(cellData, n)] = migration
                    else if (migration < 0) migrationRecord[Pair(n, cellData)] = -migration
                    processedNeighbours[cellData]?.add(n)
                }
            }
        }
        return migrationRecord
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
package dev.miguelmoreno.km.data

import co.touchlab.kermit.Logger
import dev.miguelmoreno.km.data.source.api.ApiDataSource
import dev.miguelmoreno.km.data.source.db.DbDataSource

class RunsRepository(
    private val apiDataSource: ApiDataSource,
    private val dbDataSource: DbDataSource,
) {
    private val logger = Logger.withTag("RunsRepository")

    suspend fun getRuns(): List<Run> {
        try {
            val runs = apiDataSource.getRuns()
            dbDataSource.updateRuns(runs)
        } catch (exception: ApiDataSource.NotAvailableException) {
            logger.e("Connection failed, using local data source.\n" + exception.message)
        }
        return dbDataSource.getRuns().sortedByDescending { it.startDate }
    }
}

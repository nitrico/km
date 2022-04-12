package dev.miguelmoreno.km.data

import dev.miguelmoreno.km.data.source.api.ApiDataSource

class RunsRepository(
    //private val databaseDataSource: DatabaseDataSource,
    private val apiDataSource: ApiDataSource,
) {
    suspend fun getRuns(): List<Run> {
        var runs: List<Run>
        try {
            runs = apiDataSource.getRuns()
            //databaseDataSource.updateRuns(runs)
        } catch (exception: ApiDataSource.NotAvailableException) {
            runs = emptyList()
            //Log.e("RunsRepository", "Connection failed, using local data source.\n" + exception.message)
        }
        return runs//databaseDataSource.getRuns()
    }
}

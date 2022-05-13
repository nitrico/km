package dev.miguelmoreno.km.data.source.db

import dev.miguelmoreno.km.data.Run
import io.realm.MutableRealm
import io.realm.Realm
import io.realm.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DbDataSource(private val realm: Realm) {

    fun getRuns(): List<Run> =
        realm.query<RunDbModel>().find().map { it.toRun() }

    suspend fun updateRuns(runs: List<Run>) {
        withContext(Dispatchers.Default) {
            realm.write {
                runs.forEach {
                    copyToRealm(it.toRunDbModel(), updatePolicy = MutableRealm.UpdatePolicy.ALL)
                }
            }
        }
    }
}

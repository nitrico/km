package dev.miguelmoreno.km.domain

import dev.miguelmoreno.km.data.Run
import dev.miguelmoreno.km.data.RunsRepository
import dev.miguelmoreno.km.data.User
import dev.miguelmoreno.km.data.UserRepository
import dev.miguelmoreno.km.data.source.api.StravaApi
import org.koin.core.component.inject

object LoadUser : Action {
    private val userRepository by inject<UserRepository>()

    override fun reduce(state: State): State {
        return state.copy(isLoading = true)
    }

    override suspend fun effect() {
        val user = userRepository.getUser()
        store.dispatch(UserLoaded(user))
    }
}

data class UserLoaded(private val user: User?) : Action {

    override fun reduce(state: State): State {
        return state.copy(user = user, isLoading = false)
    }
}

data class ConnectToStrava(private val authorizationCode: String) : Action {
    private val stravaApi by inject<StravaApi>()

    override fun reduce(state: State): State {
        return state.copy(isLoading = true)
    }
    override suspend fun effect() {
        stravaApi.authorize(authorizationCode) {
            store.dispatch(LoadUser)
            store.dispatch(LoadRuns)
        }
    }
}

object DisconnectFromStrava : Action {
    private val stravaApi by inject<StravaApi>()

    override fun reduce(state: State): State {
        return state.copy(user = null, isLoading = true)
    }
    override suspend fun effect() {
        stravaApi.deauthorize {
            store.dispatch(Disconnected)
        }
    }
}

object Disconnected : Action {
    override fun reduce(state: State): State = State()
}

object LoadRuns : Action {
    private val runsRepository by inject<RunsRepository>()

    override fun reduce(state: State): State {
        return state.copy(isLoading = true)
    }
    override suspend fun effect() {
        val runs = runsRepository.getRuns()
        store.dispatch(RunsLoaded(runs))
    }
}

data class RunsLoaded(private val runs: List<Run>) : Action {

    override fun reduce(state: State): State {
        val distance = runs.sumOf { it.distance } / METERS_IN_A_KM
        return state.copy(runs = runs, distance = distance, isLoading = false)
    }
    private companion object {
        const val METERS_IN_A_KM = 1000
    }
}

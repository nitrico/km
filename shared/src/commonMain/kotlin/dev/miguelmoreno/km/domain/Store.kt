package dev.miguelmoreno.km.domain

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent

fun interface Reducer {
    fun reduce(state: State): State
}

interface Action : Reducer, KoinComponent {
    suspend fun sideEffect() {}
}

object Store {
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val effectScope = CoroutineScope(Dispatchers.Main)

    private val logger = Logger.withTag("Store")

    fun dispatch(action: Action) {
        runBlocking {
            logger.d { "Dispatching $action" }
            _state.emit(action.reduce(state.value))
            logger.d { "New state: ${state.value}" }
        }
        effectScope.launch {
            action.sideEffect()
        }
    }
}

fun dispatch(action: Action) {
    Store.dispatch(action)
}

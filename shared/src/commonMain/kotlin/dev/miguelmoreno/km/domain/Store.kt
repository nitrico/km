package dev.miguelmoreno.km.domain

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent

fun interface Reducer {
    fun reduce(state: State): State
}

interface Action : Reducer, KoinComponent {
    suspend fun sideEffect() {}
}

object Store {
    private val logger = Logger.withTag("Store")
    private val mutex = Mutex()

    private val storeScope = CoroutineScope(Dispatchers.Main)
    private val effectScope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun dispatch(action: Action) {
        storeScope.launch {
            mutex.withLock {
                logger.d { "Dispatching $action" }
                _state.emit(action.reduce(state.value))
                logger.d { "New state: ${state.value}" }
            }
        }
        effectScope.launch {
            action.sideEffect()
        }
    }
}

fun dispatch(action: Action) {
    Store.dispatch(action)
}

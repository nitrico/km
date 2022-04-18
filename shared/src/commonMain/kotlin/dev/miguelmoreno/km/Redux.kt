package dev.miguelmoreno.km

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface AbsAction<State> {
    fun reduce(state: State): State
    suspend fun effect() {}
}

typealias Dispatch<Action> = (action: Action) -> Unit

fun interface MiddleWare<State, Action : AbsAction<State>> {
    operator fun invoke(
        state: State,
        action: Action,
        dispatch: Dispatch<Action>,
        next: Next<State, Action>
    ): Action
}

fun interface Next<State, Action : AbsAction<State>> {
    operator fun invoke(state: State, action: Action, dispatch: Dispatch<Action>): Action
}

class Store<State, Action : AbsAction<State>>(
    initialState: State,
    private val middleware: List<MiddleWare<State, Action>> = emptyList()
) {
    private val logger = Logger.withTag("Store")
    private val mutex = Mutex()

    private val storeScope = CoroutineScope(Dispatchers.Main)
    private val effectScope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    fun dispatch(action: Action) {
        storeScope.launch {
            val newAction = applyMiddleware(action)
            mutex.withLock {
                _state.emit(newAction.reduce(state.value))
            }
        }
        effectScope.launch {
            action.effect()
        }
    }

    private fun applyMiddleware(action: Action): Action {
        return next(0).invoke(state.value, action, ::dispatch)
    }

    private fun next(index: Int): Next<State, Action> {
        if (index == middleware.size) {
            return Next { _, action, _ -> action }
        }
        return Next { state, action, dispatch ->
            middleware[index].invoke(state, action, dispatch, next(index+1))
        }
    }
}

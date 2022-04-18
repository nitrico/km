package dev.miguelmoreno.km.domain

import co.touchlab.kermit.Logger
import dev.miguelmoreno.km.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

interface Action : AbsAction<State>, KoinComponent {
    val store: Store<State, Action>
        get() = get()
}

class LoggerMiddleWare : MiddleWare<State, Action> {
    private val logger = Logger.withTag("LoggerMiddleWare")

    override fun invoke(
        state: State,
        action: Action,
        dispatch: Dispatch<Action>,
        next: Next<State, Action>
    ): Action {
        logger.d { "Action: $action" }
        return action
    }
}

package dev.miguelmoreno.km.domain

import co.touchlab.kermit.Logger
import dev.miguelmoreno.km.Dispatch
import dev.miguelmoreno.km.Middleware
import dev.miguelmoreno.km.Next

class LoggerMiddleware : Middleware<State, Action> {

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

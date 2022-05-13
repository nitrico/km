package dev.miguelmoreno.km.domain

import dev.miguelmoreno.km.BaseStore

class Store : BaseStore<State, Action>(
    initialState = State(),
    middleware = listOf(
        LoggerMiddleware()
    )
)

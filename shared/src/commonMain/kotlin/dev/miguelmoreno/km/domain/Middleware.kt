package dev.miguelmoreno.km.domain

class DispatchChain {

}

typealias Middleware = (State, Action, DispatchChain) -> Unit

class SomeMiddleware(
    // dependencies...
) : Middleware {
    override fun invoke(state: State, action: Action, chain: DispatchChain) {
        when (action) {
            //...
            // chain.next(action) // pass it to the next middleware or swallow it or send another action instead...
        }
    }
}

enum class EffectMode { DEFAULT, IMMEDIATE, SINGLE, CONFLATED, RESTART }

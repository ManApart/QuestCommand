package interact.scope

import core.gameState.GameState
import core.gameState.location.LocationNode

object ScopeManager {
    private val scopes = mutableMapOf<LocationNode, Scope>()

    init {
        reset()
    }

    fun reset() {
        scopes.clear()
    }

    fun getScope(targetLocation: LocationNode? = null) : Scope {
        val location = targetLocation ?: GameState.player.location
        if (!scopes.containsKey(location)){
            scopes[location] = Scope(location)
        }
        return scopes[location]!!
    }


}
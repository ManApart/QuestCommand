package travel.climb

import core.events.Event
import core.gameState.GameState
import core.gameState.Target
import core.gameState.location.LocationNode

class AttemptClimbEvent(val creature: Target = GameState.player, val target: Target, val targetPart: LocationNode) : Event
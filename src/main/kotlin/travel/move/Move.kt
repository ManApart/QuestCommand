package travel.move

import combat.battle.Distances.LOCATION_SIZE
import core.events.EventListener
import core.gameState.GameState
import core.gameState.NO_VECTOR
import core.gameState.Target
import core.gameState.Vector
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint
import core.history.display
import core.history.displayIf
import core.utility.StringFormatter.getIsAre
import core.utility.StringFormatter.getSubject
import interact.scope.ScopeManager
import system.EventManager
import travel.ArriveEvent

class Move : EventListener<MoveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: MoveEvent) {
        val movedToNeighbor = getMovedToNeighbor(event.creature.location, event.destination)
        when {
            event.destination.z > 0 -> display("${getSubject(event.creature)} ${getIsAre(event.creature)} unable to move into the air.")
            movedToNeighbor != null -> EventManager.postEvent(ArriveEvent(destination = movedToNeighbor, method = "travel"))
            NO_VECTOR.getDistance(event.destination) > LOCATION_SIZE -> display("You cannot move that far in that direction.")
            else -> {
                displayMovement(event)
                event.creature.position = event.destination
            }
        }
    }

    private fun displayMovement(event: MoveEvent) {
        if (!event.silent) {
            if (event.creature.isPlayer()) {
                display("You move from ${event.source} to ${event.destination}")
            } else {
                display("${event.creature} moves from ${event.source} to ${event.destination}")
            }
        }
    }


    private fun getMovedToNeighbor(locationNode: LocationNode, destination: Vector): LocationPoint? {
        return locationNode.getNeighborConnections()
                .filter { destination.isFurtherAlongSameDirectionThan(it.vector) }
                .minBy { destination.getDistance(it.vector) }
                ?.destination
    }

}
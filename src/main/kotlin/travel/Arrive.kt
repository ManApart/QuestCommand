package travel

import core.events.EventListener
import core.gameState.GameState
import core.history.display

class Arrive : EventListener<ArriveEvent>() {
    override fun execute(event: ArriveEvent) {
        if (event.origin != event.destination) {
            GameState.player.location = event.destination.location
            if (!event.silent) {
                display("You ${event.method} to ${event.destination}. It ${event.destination.location.getSiblings()}.")
            }
        }
    }

}
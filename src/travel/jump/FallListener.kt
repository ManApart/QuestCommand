package travel.jump

import core.commands.CommandParser
import core.events.EventListener
import core.gameState.GameState
import core.gameState.Stat
import status.StatChangeEvent
import system.EventManager
import travel.ArriveEvent

class FallListener : EventListener<FallEvent>() {
    override fun shouldExecute(event: FallEvent): Boolean {
        return event.creature == GameState.player
    }

    override fun execute(event: FallEvent) {
        if (event.reason != null) println(event.reason)
        println("You fall ${event.fallHeight}ft.")
        takeDamage(event)
        GameState.journey = null
        if (GameState.player.location != event.destination){
            EventManager.postEvent(ArriveEvent(destination = event.destination, method = "fall"))
        }
    }

    private fun takeDamage(event: FallEvent) {
        //TODO add defense per their foot defense
        val amount = Math.max(0, event.fallHeight - event.creature.soul.getCurrent(Stat.AGILITY))
        if (amount != 0) {
            EventManager.postEvent(StatChangeEvent(event.creature, "Falling", Stat.HEALTH, -amount))
        } else {
            println("You land without taking damage.")
        }

    }
}
package interact.actions

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import core.history.display
import interact.UseEvent
import status.effects.AddEffectEvent
import status.effects.EffectManager
import system.EventManager

class StartFire : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return if (GameState.player.canInteract && event.target is Activator) {
            event.source.properties.tags.has("Fire Starter") && event.target.properties.tags.has("Flammable")
        } else {
            false
        }
    }

    override fun execute(event: UseEvent) {
        display("${event.target.name} catches on fire.")
        val creature = (event.target as Activator).creature
        EventManager.postEvent(AddEffectEvent(creature, EffectManager.getEffect("Burning")))
        EventManager.postEvent(AddEffectEvent(creature, EffectManager.getEffect("On Fire")))
    }
}
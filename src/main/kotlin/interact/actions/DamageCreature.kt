package interact.actions

import core.events.EventListener
import core.gameState.stat.HEALTH
import interact.UseEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class DamageCreature : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.used.properties.tags.has("Weapon") && event.target.soul.hasStat(HEALTH)
    }

    override fun execute(event: UseEvent) {
        EventManager.postEvent(StatChangeEvent(event.target, event.used.name, "Health", -event.used.getDamage()))
    }
}
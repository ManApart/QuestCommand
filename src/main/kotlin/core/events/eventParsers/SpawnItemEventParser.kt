package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import traveling.scope.spawn.SpawnItemEvent

class SpawnItemEventParser : EventParser {
    override fun className(): String {
        return SpawnItemEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val itemNameP = 0
        val countP = 1
        val targetP = 2
        val targetLocationNetworkP = 3
        val targetLocationP = 4
        val positionParentP = 5

        val targetLocation = event.getLocation(parent, targetLocationNetworkP, targetLocationP)
        val target = event.getTargetCreature(parent, targetP, targetLocation)
        val positionParent = event.getTargetCreature(parent, positionParentP) ?: parent

        return SpawnItemEvent(event.getParam(itemNameP), event.getParamInt(countP), target, targetLocation, positionParent = positionParent)
    }
}
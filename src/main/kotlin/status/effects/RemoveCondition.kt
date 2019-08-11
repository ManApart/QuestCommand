package status.effects

import core.events.EventListener
import core.history.display

class RemoveCondition: EventListener<RemoveConditionEvent>() {
    override fun shouldExecute(event: RemoveConditionEvent): Boolean {
        return true
    }

    override fun execute(event: RemoveConditionEvent) {
        event.target.soul.removeCondition(event.condition)
        display("${event.target} is no longer ${event.condition.name}")
    }
}
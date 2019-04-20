package inventory.unEquipItem

import core.events.Event
import core.gameState.Target

class UnEquipItemEvent(val creature: Target, val item: Target) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}
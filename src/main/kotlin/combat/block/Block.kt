package combat.block

import combat.DamageType
import core.events.EventListener
import core.GameState
import core.target.Target
import traveling.location.location.Location

class Block : EventListener<BlockEvent>() {

    override fun execute(event: BlockEvent) {
        if (GameState.battle != null) {
            val combatant = GameState.battle!!.getCombatant(event.source)
            if (combatant != null) {
                combatant.blockBodyPart = event.partThatWillShield
                combatant.blockedBodyParts.addAll(getBlockedParts(event))
            }
        }
    }

    private fun getBlockedParts(event: BlockEvent): List<Location> {
        val shield = getShield(event.partThatWillShield)
        val shieldSize = shield?.properties?.values?.getInt("radius") ?: 0
        val partLocation = event.source.body.layout.findLocation(event.partThatWillBeShielded.name)
        val locations = listOf(partLocation) + partLocation.getNeighbors().filter { partLocation.getConnection(it)?.vector?.getDistance() ?: 0 <= shieldSize }

        return locations.map { it.getLocation() }
    }

    private fun getShield(partThatWillShield: Location): Target? {
        val equippedItems = partThatWillShield.getEquippedItems()
        return equippedItems.firstOrNull { it.properties.tags.has("shield") }
                ?: equippedItems.maxBy { getTotalDefense(it) }
    }

    private fun getTotalDefense(apparel: Target): Int {
        val values = apparel.properties.values
        return values.getInt("defense") + DamageType.values().sumBy { values.getInt(it.defense) }
    }

}
package status

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.Stat
import core.history.display
import core.utility.StringFormatter

class Status : EventListener<StatusEvent>() {

    override fun execute(event: StatusEvent) {
        printImportantStats(event)
        printOtherStats(event)
    }

    private fun printImportantStats(event: StatusEvent) {
        val soul = event.creature.soul
        if (soul.hasStat("Health") || soul.hasStat("Stamina")) {
            val subject = StringFormatter.format(event.creature == GameState.player.creature, "You have", "${event.creature.name} has")
            display("$subject ${soul.getCurrent("Health")}/${soul.getTotal("Health")} HP and ${soul.getCurrent("Stamina")}/${soul.getTotal("Stamina")} Stamina.")
        }
    }

    private fun printOtherStats(event: StatusEvent) {
        val soul = event.creature.soul
        val subject = StringFormatter.getSubjectPossessive(event.creature)
        val statString = soul.getStats().asSequence().filter { it != soul.getStatOrNull(Stat.HEALTH) && it != soul.getStatOrNull(Stat.STAMINA) }.joinToString("\n\t") {
            "${it.name.capitalize()}: ${it.current}/${it.baseMax} (${it.getCurrentXP().toInt()}/${it.getNextLevelXP().toInt()}xp)"
        }
        display("$subject stats are:\n\t$statString")
    }

}
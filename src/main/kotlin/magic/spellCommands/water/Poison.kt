package magic.spellCommands.water

import combat.battle.Distances
import combat.battle.position.TargetAim
import combat.battle.position.toCommandString
import core.commands.Args
import core.commands.ResponseRequest
import core.commands.ResponseRequestHelper
import core.commands.ResponseRequestWrapper
import core.target.Target
import status.stat.WATER_MAGIC
import magic.castSpell.StartCastSpellEvent
import magic.castSpell.getTargetedPartsOrRootPart
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import magic.Element
import core.events.EventManager

class Poison : SpellCommand() {
    override val name = "Poison"

    override fun getDescription(): String {
        return "Cast Poison:\n\tPoison a target, doing damage over time."
    }

    override fun getManual(): String {
        return "\n\tCast Poison <amount> for <duration> on *<targets> - Does damage over time."
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialAmount = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")

        val options = listOf("1", "3", "5", "10", "50", "#")
        val amountResponse = ResponseRequest("Poison how much?", options.map { it to "cast poison $it for ${initialDuration.toString()} on ${targets.toCommandString()}" }.toMap())
        val durationResponse = ResponseRequest("Poison for how long?", options.map { it to "cast poison ${initialAmount.toString()} for $it on ${targets.toCommandString()}" }.toMap())
        val responseHelper = ResponseRequestHelper(mapOf(
                "amount" to ResponseRequestWrapper(initialAmount, amountResponse, useDefaults, 1),
                "duration" to ResponseRequestWrapper(initialDuration, durationResponse, useDefaults, 10)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val amount = responseHelper.getIntValue("amount")
            val duration = responseHelper.getIntValue("duration")
            val hitCount = targets.count()
            val totalCost = (amount * hitCount * 2) + (duration / 4)
            val levelRequirement = amount/2

            executeWithWarns(source, WATER_MAGIC, levelRequirement, totalCost, targets) {
                targets.forEach { target ->
                    val parts = getTargetedPartsOrRootPart(target)
                    val effects = listOf(
                            EffectManager.getEffect("Poison", amount, duration, parts),
                            EffectManager.getEffect("Wet", 0, duration + 1, parts)
                    )

                    val condition = Condition("Poisoned", Element.WATER, amount, effects)
                    val spell = Spell("Poison", condition, amount, WATER_MAGIC, levelRequirement, range = Distances.SWORD_RANGE)
                    EventManager.postEvent(StartCastSpellEvent(source, target, spell))
                }
            }
        }
    }

}
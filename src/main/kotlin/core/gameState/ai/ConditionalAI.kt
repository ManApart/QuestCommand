package core.gameState.ai

import combat.DamageType
import combat.attack.StartAttackEvent
import combat.battle.position.TargetAim
import core.gameState.GameState
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import system.EventManager

class ConditionalAI(name: String, creature: Target, val actions: List<TriggeredEvent>) : AI(name, creature) {

    override fun takeAction() {
        //TODO - replace hardcoding with script informed / generic
        if (actions.isEmpty()) {
            defaultHardCodedAction()
        } else {
            //TODO - evaluate triggered events
        }
    }

    private fun defaultHardCodedAction() {
        if (GameState.battle != null) {
            val playerBody = GameState.player.body
            val possibleParts = listOf(
                    playerBody.getPart("Right Leg"),
                    playerBody.getPart("Right Foot"),
                    playerBody.getPart("Left Leg"),
                    playerBody.getPart("Left Leg")
            )
            val targetPart = listOf(possibleParts.random())
            val defaultPart = if (creature.body.hasPart("Small Claws")) {
                creature.body.getPart("Small Claws")
            } else {
                creature.body.getRootPart() ?: creature.body.getParts().firstOrNull()
            }
            if (defaultPart != null) {
                EventManager.postEvent(StartAttackEvent(creature, defaultPart, TargetAim(GameState.player, targetPart), DamageType.SLASH))
            }
        }
    }

}
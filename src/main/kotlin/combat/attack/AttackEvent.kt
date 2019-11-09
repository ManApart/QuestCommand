package combat.attack

import combat.DamageType
import combat.battle.position.TargetAim
import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart


class AttackEvent(val source: Target, val sourcePart: BodyPart, val target: TargetAim, val type: DamageType) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}
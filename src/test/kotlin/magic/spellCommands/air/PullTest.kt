package magic.spellCommands.air

import combat.DamageType
import traveling.position.TargetAim
import core.DependencyInjector
import core.GameState
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.BodyManager
import core.commands.Args
import core.events.EventManager
import core.properties.WEIGHT
import core.target.Target
import magic.castSpell.StartCastSpellEvent
import magic.spellCommands.SpellCommandsMock
import magic.spellCommands.SpellCommandsCollection
import magic.spells.MoveTargetSpell
import org.junit.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import status.effects.EffectBase
import status.effects.EffectFakeParser
import status.effects.EffectManager
import status.effects.EffectParser
import status.stat.AIR_MAGIC
import status.stat.FOCUS
import status.stat.StatEffect
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.position.NO_VECTOR
import traveling.position.Vector
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import org.testng.Assert.assertEquals
import kotlin.test.assertNotNull

class PullTest {

    companion object {
        init {
            DependencyInjector.setImplementation(BehaviorsCollection::class.java, BehaviorsMock())
            DependencyInjector.setImplementation(SpellCommandsCollection::class.java, SpellCommandsMock())

            DependencyInjector.setImplementation(LocationParser::class.java, LocationFakeParser())
            LocationManager.reset()

            DependencyInjector.setImplementation(LocationParser::class.java, BodyFakeParser())
            BodyManager.reset()

            DependencyInjector.setImplementation(EffectParser::class.java, EffectFakeParser(listOf(
                    EffectBase("Air Blasted", "", "Health", statEffect = StatEffect.RECOVER, damageType = DamageType.AIR)
            )))
            EventManager.reset()
            EffectManager.reset()
        }

        private val caster = Target("caster")
        private val victim = Target("victim")
        private val scope = GameState.currentLocation()

        init {
            scope.addTarget(caster)
            scope.addTarget(victim)
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            DependencyInjector.clearAllImplementations()
            EventManager.clear()
        }
    }

    @Before
    fun setup() {
        EventManager.clear()
        caster.position = NO_VECTOR
        victim.position = Vector(0, 10, 0)
        caster.soul.setStat(AIR_MAGIC, 100)
        caster.soul.addStat(FOCUS, 10, 100, 1)
    }

    @Test
    fun pullFromSamePosition() {
        val spell = castSpell("cast pull 20 on victim")

        assertEquals(Vector(0, 0, 0), spell.vector)
    }

    @Test
    fun pullIsDependantOnPowerAndWeight() {
        victim.properties.values.put(WEIGHT, 2)
        val spell = castSpell("cast pull 10 on victim")
        victim.properties.values.clear(WEIGHT)

        assertEquals(Vector(0, 5, 0), spell.vector)
    }

    @Test
    fun pullFromDifferentPosition() {
        victim.position = Vector(y = 15)
        val spell = castSpell("cast pull 5 on victim")

        assertEquals(Vector(0, 10, 0), spell.vector)
    }

    @Test
    fun pullInDirectionFromSamePosition() {
        val spell = castSpell("cast pull 10 towards west on victim")

        assertEquals(Vector(-10, 10, 0), spell.vector)
    }

    @Test
    fun pullInDifferentDirectionFromSamePosition() {
        val spell = castSpell("cast pull 5 towards south on victim")

        assertEquals(Vector(0, 5, 0), spell.vector)
    }

    @Test
    fun pullInDirectionFromDifferentPosition() {
        victim.position = Vector(y = 15)
        val spell = castSpell("cast pull 10 towards east on victim")

        assertEquals(Vector(10, 15, 0), spell.vector)
    }

    //Allow pulling to an exact location?
//    @Test
//    fun pullTowardsVector() {
//        val spell = castSpell("cast pull towards (20, 0, 0) on victim")
//
//        assertEquals(Vector(20, 0, 0), spell.vector)
//    }

    private fun castSpell(input: String): MoveTargetSpell {
        val args = Args(input.split(" "), delimiters = listOf("on"))
        Pull().execute(caster, args, listOf(TargetAim(victim)), false)
        val spell = (EventManager.getUnexecutedEvents().firstOrNull() as StartCastSpellEvent).spell as MoveTargetSpell?
        assertNotNull(spell)
        return spell
    }

}
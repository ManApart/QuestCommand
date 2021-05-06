package magic.spellCommands.water

import combat.DamageType
import traveling.position.TargetAim
import core.DependencyInjector
import core.GameState
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.BodyManager
import core.commands.Args
import core.events.EventManager
import core.target.Target
import magic.SpellCommandMock
import magic.castSpell.StartCastSpellEvent
import magic.spellCommands.SpellCommandsMock
import magic.spellCommands.SpellCommandsCollection
import magic.spells.Spell
import org.junit.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import status.effects.EffectBase
import status.effects.EffectFakeParser
import status.effects.EffectManager
import status.effects.EffectParser
import status.stat.FOCUS
import status.stat.StatEffect
import status.stat.WATER_MAGIC
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import org.testng.Assert.assertEquals
import kotlin.test.assertNotNull
import org.testng.Assert.assertTrue

class HealTest {
    companion object {
        init {
            DependencyInjector.setImplementation(BehaviorsCollection::class.java, BehaviorsMock())
            DependencyInjector.setImplementation(SpellCommandsCollection::class.java, SpellCommandsMock())

            DependencyInjector.setImplementation(LocationParser::class.java, LocationFakeParser())
            LocationManager.reset()

            DependencyInjector.setImplementation(LocationParser::class.java, BodyFakeParser())
            BodyManager.reset()

            DependencyInjector.setImplementation(EffectParser::class.java, EffectFakeParser(listOf(
                    EffectBase("Heal", "", "Health", statEffect = StatEffect.RECOVER, damageType = DamageType.WATER),
                    EffectBase("Wet", "", statTarget = "Agility", statEffect = StatEffect.DEPLETE, damageType = DamageType.WATER)
            )))
            EventManager.reset()
            EffectManager.reset()
        }

        private val targetA = Target("targetA")
        private val scope = GameState.currentLocation()

        init {
            scope.addTarget(targetA)
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
        targetA.soul.setStat(WATER_MAGIC, 20)
        targetA.soul.addStat(FOCUS, 10, 100, 1)
    }

    @Test
    fun defaultArgs() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class.java, reflections)

        val spell = castHeal("")

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.targets.isEmpty())

        assertNotNull(spell)
        assertEquals(5, spell.condition.elementStrength)
        assertEquals(1, spell.condition.getEffects()[0].duration)
    }

    @Test
    fun amountAndDuration() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class.java, reflections)

        val spell = castHeal("5 for 10")

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.targets.isEmpty())

        assertNotNull(spell)
        assertEquals(5, spell.condition.elementStrength)
        assertEquals(10, spell.condition.getEffects()[0].duration)
    }


    private fun castHeal(input: String): Spell {
        val args = Args(input.split(" "), delimiters = listOf("on"))
        Heal().execute(targetA, args, listOf(TargetAim(targetA)), true)
        return (EventManager.getUnexecutedEvents().firstOrNull() as StartCastSpellEvent).spell
    }


}
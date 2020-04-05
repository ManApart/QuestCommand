package combat

import combat.attack.AttackCommand
import combat.attack.StartAttackEvent
import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.AIFakeParser
import core.ai.AIManager
import core.ai.AIParser
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorParser
import core.body.BodyManager
import core.events.EventManager
import core.target.Target
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import traveling.location.weather.WeatherFakeParser
import traveling.location.weather.WeatherManager
import traveling.location.weather.WeatherParser
import kotlin.test.assertEquals

class AttackCommandTest {
    private val command = AttackCommand()

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser.parserWithFakePlayer()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val aiParser = AIFakeParser()
        DependencyInjector.setImplementation(AIParser::class.java, aiParser)
        AIManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        val weatherParser = WeatherFakeParser()
        DependencyInjector.setImplementation(WeatherParser::class.java, weatherParser)
        WeatherManager.reset()

        EventManager.clear()

        GameState.player = GameManager.newPlayer()
    }

    @Test
    fun attackCreatureWithoutDirection() {
        val rat = Target("Rat", bodyName = "human")
        GameState.currentLocation().addTarget(rat)

        command.execute("sl", "rat".split(" "))
        val event = EventManager.getUnexecutedEvents()[0] as StartAttackEvent
        assertEquals(rat, event.target.target)
    }
}
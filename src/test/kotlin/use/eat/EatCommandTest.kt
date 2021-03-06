package use.eat

import core.GameManager
import core.GameState
import core.commands.CommandParser
import core.events.EventManager
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.target.item.ITEM_TAG
import core.utility.PoorMansInstrumenter
import createMockedGame
import org.junit.Before
import org.junit.Test
import traveling.location.location.NOWHERE_NODE
import use.StartUseEvent
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EatCommandTest {

    @Before
    fun setup() {
        createMockedGame()

        GameState.player = GameManager.newPlayer(location = NOWHERE_NODE)
    }

    @Test
    fun eatFood() {
        val timer = PoorMansInstrumenter(10000)
        val item = Target("Pear", properties = Properties(tags = Tags(listOf("Food", ITEM_TAG))))
        timer.printElapsed("new item")
        GameState.player.inventory.add(item)
        timer.printElapsed("add item")
        EatCommand().execute("eat", listOf("Pear"))
        timer.printElapsed("execute event")
        val events = EventManager.getUnexecutedEvents()
        timer.printElapsed("get events")

        assertEquals(1, events.size)
        assertTrue(events[0] is StartUseEvent)
        assertEquals(item, (events[0] as StartUseEvent).used)
        assertNull(CommandParser.getResponseRequest())
    }

    @Test
    fun eatMultipleFoodGivesChoice() {
        val fruit = Target("Pear", properties = Properties(tags = Tags(listOf("Food", ITEM_TAG))))
        val pie = Target("Pear Pie", properties = Properties(tags = Tags(listOf("Food", ITEM_TAG))))
        GameState.player.inventory.add(fruit)
        GameState.player.inventory.add(pie)
        EatCommand().execute("eat", listOf("Pear"))
        val events = EventManager.getUnexecutedEvents()

        assertEquals(0, events.size)
        assertNotNull(CommandParser.getResponseRequest())
    }
}

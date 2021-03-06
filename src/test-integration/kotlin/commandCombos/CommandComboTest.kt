package commandCombos

import core.GameManager
import core.GameState
import core.commands.CommandParser
import core.events.EventManager
import core.history.ChatHistory
import org.junit.Before
import org.junit.Test
import quests.QuestManager
import status.stat.HEALTH
import system.debug.DebugType
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CommandComboTest {

    @Before
    fun reset() {
        EventManager.clear()
        EventManager.registerListeners()
        GameManager.newGame(testing = true)
        EventManager.executeEvents()
    }

    @Test
    fun sliceApple() {
        val input = "use dagger on apple"
        CommandParser.parseCommand(input)
        assertTrue(GameState.player.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.inventory.getItem("Apple")?.properties?.tags?.has("Sliced") ?: false)
    }

    @Test
    fun chopApple() {
        val input = "chop apple"
        CommandParser.parseCommand(input)
        assertTrue(GameState.player.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.inventory.getItem("Apple")?.properties?.tags?.has("Sliced") ?: false)
    }

    @Test
    fun eatApple() {
        val input = "sl head of self && eat apple && n && eat apple"
        CommandParser.parseCommand(input)
        assertNull(GameState.player.inventory.getItem("Apple"))
        assertEquals("You feel the fullness of life beating in your bosom.", ChatHistory.getLastOutput())
    }

    @Test
    fun roastApple() {
        val input = "w && s && rs 10 && move to range && pickup tinder box && n && e && n && rs 10 && use tinder on tree && use apple on tree"
        CommandParser.parseCommand(input)
        assertTrue(GameState.player.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.inventory.getItem("Apple")?.properties?.tags?.has("Roasted") ?: false)
    }

    @Test
    fun cookApple() {
        val stat = GameState.player.soul.getStats().first { it.name.lowercase() == "cooking" }
        stat.setLevel(2)

        val input = "w && s && move to range && cook apple on range"
        CommandParser.parseCommand(input)
        assertTrue(GameState.player.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.inventory.getItem("Apple")?.properties?.tags?.has("Cooked") ?: false)
    }

    @Test
    fun makeFire() {
        val input = "n && pickup hatchet && ch tree && ch tree"
        CommandParser.parseCommand(input)
        assertEquals(0, GameState.currentLocation().getActivators("tree").size)
        assertEquals(1, GameState.currentLocation().getActivators("logs").size)
        CommandParser.parseCommand("cast flame 1 on logs")
        assertTrue(GameState.currentLocation().getActivators("logs").first().soul.hasEffect("On Fire"))
        CommandParser.parseCommand("eat apple && eat apple && cast flame 1 on logs && rest 3")
        assertEquals(0, GameState.currentLocation().getActivators("logs").size)
        assertEquals(1, GameState.currentLocation().getItems("ash").size)
    }

    @Test
    fun climbTree() {
        val input = "db random && n && climb tree && climb && d && d"
        CommandParser.parseCommand(input)
        assertTrue(ChatHistory.history[1].outPut.contains("You Climb to Apple Tree Branches. It is neighbored by Apple Tree (BELOW)."))
        assertTrue(ChatHistory.getLastOutputs().contains("You climb back off Apple Tree."))
    }

    @Test
    fun fightRat() {
        CommandParser.parseCommand("s")
        val input = "slash body of rat && r && r"
        CommandParser.parseCommand(input)
        assertEquals("Rat has died.", ChatHistory.getLastOutput())
    }

    @Test
    fun ratFightsBack() {
        GameState.properties.values.put(DebugType.RANDOM_SUCCEED.propertyName, true)
        GameState.properties.values.put(DebugType.RANDOM_RESPONSE.propertyName, 0)
        CommandParser.parseCommand("s && nothing && nothing && nothing && nothing && nothing")
ChatHistory.getLastInput()
        assertEquals("Oh dear, you have died!", ChatHistory.getLastOutput())
    }

    @Test
    fun doNotAttackDeadThing() {
        val input = "s && slash body of rat && sl rat && sl && slash rat"
        CommandParser.parseCommand(input)
        val expected = "slash what with Rusty Dagger?\n\tPlayer, Poor Quality Meat"
        assertEquals(expected, ChatHistory.getLastOutput())
    }

    @Test
    fun useGate() {
        val input = "w && w && use gate"
        CommandParser.parseCommand(input)
        assertEquals("You can now access Kanbara City from Kanbara Gate.", ChatHistory.getLastOutput())
    }

    @Test
    fun enterKanbaraThroughGate() {
        val input = "w && w && rs 10 && use gate && w"
        CommandParser.parseCommand(input)
        assertEquals("You travel to Kanbara City. It is neighbored by Kanbara Gate (EAST), Kanbara Pub, Mapmaker Manor, Kanbara City South, Kanbara Wall North (SOUTH).", ChatHistory.getLastOutput())
    }

    @Test
    fun enterKanbaraThroughWall() {
        val input = "w && w && rs 10 && sw && rs 10 && cl && cl && cl && cl && d && d && d && ls"
        CommandParser.parseCommand(input)
        assertEquals("You are at Kanbara City South", ChatHistory.getCurrent().outPut[3])
    }

    @Test
    fun millFlour() {
        val input = "move to wheat && slash wheat && pickup wheat && ne && a && a && put wheat in chute && d && d && take wheat from bin"
        CommandParser.parseCommand(input)
        assertEquals("Player picked up Wheat Flour.", ChatHistory.getLastOutput())
    }

    @Test
    fun makePie() {
        val input = "move to wheat && slash wheat && pickup wheat && t hut && take bucket && use bucket on well && t windmill && t" +
                "&& a && a && put wheat in chute && d && d && take wheat from bin && use flour on bucket" +
                "&& use dagger on apple" +
                "&& t interior && t && t && rest 10 && move to range && take pie tin" +
                "&& read recipe && rs" +
                "&& take box && use box on range && craft apple pie"
        CommandParser.parseCommand(input)

        assertNotNull(GameState.player.inventory.getItem("Apple Pie"))
        assertEquals(true, QuestManager.quests.get("A Simple Pie").complete)
    }

    @Test
    fun tutorial() {
        val input = "help commands && journal && m && w && s && ls && mv 0,10,0 && read recipe "
        CommandParser.parseCommand(input)

        assertEquals(true, QuestManager.quests.get("Tutorial").complete)
    }

    @Test
    fun viewMapByAliasIgnoringResponseRequest() {
        val input = "commands && m"
        CommandParser.parseCommand(input)

        val expected = """
            An Open Field is a part of Kanbara Countryside. It is neighbored by:
              Name             Distance  Direction Path  
              Farmer's Hut     100       W               
              Apple Tree       100       N               
              Barren Patch     100       S               
              Training Circle  100       E               
              Windmill         180       NE
              """.trimIndent().trim()

        assertEquals(expected, ChatHistory.getLastOutput().trimIndent().trim())
    }

    @Test
    fun poisonSelf() {
        CommandParser.parseCommand("poison 1 for 5 on head of self")
        assertEquals(1, GameState.player.soul.getConditions().size)
        assertEquals("Poison decreases Your Health by 1 (9/10).", ChatHistory.getLastOutput())

        CommandParser.parseCommand("wait 5")
        CommandParser.parseCommand("wait 1")
        assertEquals(0, GameState.player.soul.getConditions().size)
        assertEquals(5, GameState.player.soul.getCurrent(HEALTH))
        assertEquals("Player is no longer Poisoned.", ChatHistory.getLastOutput())
    }

    @Test
    fun feelTheRain() {
        CommandParser.parseCommand("rest 1 && debug weather gentle rain && rest 1")
        assertEquals(1, GameState.player.soul.getConditions().size)
        assertEquals("Rain Wet", GameState.player.soul.getConditions().first().name)
    }
}
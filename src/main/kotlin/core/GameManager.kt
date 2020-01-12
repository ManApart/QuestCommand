package core

import core.commands.CommandParser
import core.events.EventListener
import core.events.EventManager
import core.history.ChatHistory
import core.history.display
import core.properties.CAN_INTERACT
import core.properties.CAN_REST
import core.properties.CAN_TRAVEL
import core.target.Target
import core.target.item.ItemManager
import dialogue.DialogueOptions
import quests.QuestManager
import status.stat.*
import system.message.MessageEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode
import traveling.location.location.LocationPoint
import traveling.scope.ScopeManager

const val PLAYER_START_NETWORK = "Kanbara Countryside"
const val PLAYER_START_LOCATION = "An Open Field"


object GameManager {
    var playing = false

    fun newGame(gameName: String = "Kanbara", playerName: String = "Player") {
        CommandParser.reset()
        ChatHistory.reset()
        GameState.reset()
        QuestManager.reset()
        ScopeManager.reset()
        EventManager.reset()
        GameState.reset()
//        LocationManager.clear()
        setDefaultProperties()


        GameState.player = newPlayer(playerName)
        giveStartingItems(GameState.player)
        EventManager.postEvent(ArriveEvent(destination = LocationPoint(GameState.player.location), method = "wake"))
        playing = true
        EventManager.postEvent(GameStartEvent())
    }

    private fun setDefaultProperties() {
        //        GameState.properties.values.put(AUTO_SAVE, true)
        GameState.properties.values.put(SKIP_SAVE_STATS, true)
    }

    fun newPlayer(
            name: String = "Player",
            dynamicDescription: DialogueOptions = DialogueOptions("Our Hero!"),
            body: String = "Human",
            location: LocationNode = LocationManager.getNetwork(PLAYER_START_NETWORK).findLocation(PLAYER_START_LOCATION)
    ): Target {
        val player = Target(name = name, dynamicDescription = dynamicDescription, aiName = core.ai.PLAYER_CONTROLLED_ID, bodyName = body, location = location)

        with(player.soul) {
            addStat(HEALTH, 1, 10, 1)
            addStat(PERCEPTION, 1, 1, 1)
            addStat(STAMINA, 1, 100, 1)
            addStat(FOCUS, 1, 100, 1)
            addStat(STRENGTH, 1, 1, 1)
            addStat(WISDOM, 1, 1, 1)
            addStat(CLIMBING, 1)
            addStat(AGILITY, 1)
            addStat(COOKING, 1)
        }

        with(player.properties.values) {
            put(CAN_REST, true)
            put(CAN_TRAVEL, true)
            put(CAN_INTERACT, true)
        }

        with(player.properties.tags) {
            add("Open")
            add("Container")
            add("Creature")
        }

        return player
    }

    private fun giveStartingItems(player: Target) {
        val inventory = player.inventory
        val body = player.body
        listOf("Brown Pants", "Old Shirt", "Rusty Dagger", "Small Pouch").forEach {
            val item = ItemManager.getItem(it)
            inventory.add(item)
            body.equip(item)
        }
        listOf("Apple").forEach {
            val item = ItemManager.getItem(it)
            inventory.add(item)
        }
    }

    class MessageHandler : EventListener<MessageEvent>() {
        override fun execute(event: MessageEvent) {
            display(event.message)
        }

    }

}
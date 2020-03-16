package core

import combat.battle.Battle
import core.properties.Properties
import time.TimeManager
import traveling.location.location.Location

object GameState {
    var gameName = "Kanbara"
    var player = GameManager.newPlayer()
    var battle: Battle? = null
    var properties = Properties()
    val timeManager = TimeManager()


    fun reset() {
        player = GameManager.newPlayer()
        battle = null
        properties = Properties()
    }

    fun currentLocation() : Location {
        return player.location.getLocation()
    }

}


const val AUTO_SAVE = "autosave"
const val AUTO_LOAD = "autoload"
const val SKIP_SAVE_STATS = "skip save stats"
const val LAST_SAVE_CHARACTER_NAME = "last save character name"
const val LAST_SAVE_GAME_NAME = "last save character name"


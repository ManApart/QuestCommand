package explore.map

import core.commands.Command
import core.gameState.GameState
import core.history.display
import system.EventManager
import system.location.LocationManager

class ReadMapCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Map", "m")
    }

    override fun getDescription(): String {
        return "Map:\n\tGet information on your current and other locations"
    }

    override fun getManual(): String {
        return "\n\tMap *<location> - List your current location (or given location) and the surrounding areas."
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        when{
            args.isEmpty() -> currentLocation()
            else -> targetLocation(args)
        }
    }


    private fun currentLocation(){
        EventManager.postEvent(ReadMapEvent(GameState.player.creature.location))
    }

    private fun targetLocation(args: List<String>){
        val target = LocationManager.findLocation(args.joinToString(" "))
        if (target != LocationManager.NOWHERE_NODE){
            EventManager.postEvent(ReadMapEvent(target))
        } else {
            display("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

}
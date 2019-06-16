package explore.map

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.gameState.location.NOWHERE_NODE
import core.gameState.location.Network
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
        return "\n\tMap *<location> - List your current location (or given location) and the surrounding areas." +
                "\n\tMap *depth - List neighbors to <depth> levels away from the location."
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args)
        val depth = arguments.getNumber() ?: 1
        val otherArgs = args.minus(depth.toString())

        when{
            arguments.isEmpty() && keyword == "map" -> clarifyDepth()
            otherArgs.isEmpty() -> currentLocation(depth)
            else -> targetLocation(otherArgs, depth)
        }
    }

    private fun clarifyDepth() {
        val targets = listOf("1", "3", "5", "10", "20")
        display("View how many hops?\n\t${targets.joinToString(", ")}")
        CommandParser.responseRequest = ResponseRequest(targets.map { it to "map $it" }.toMap())
    }

    private fun currentLocation(depth: Int){
        EventManager.postEvent(ReadMapEvent(GameState.player.location, depth))
    }

    private fun targetLocation(args: List<String>, depth: Int){
        val target = LocationManager.findLocationInAnyNetwork(args.joinToString(" "))
        if (target != null) {
            EventManager.postEvent(ReadMapEvent(target, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

}
package inventory.dropItem

import core.GameState
import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.filterUniqueByName

class PlaceItemCommand : core.commands.Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("Place", "Give", "Put")
    }

    override fun getDescription(): String {
        return "Place:\n\tPlace an item from your inventory in another container."
    }

    override fun getManual(): String {
        return "\n\tPlace <item> in <target> - Place an item from your inventory into another container."
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "in")))
        val arguments = Args(args, delimiters)
        when {
            arguments.isEmpty() && keyword == "place" -> clarifyItemToPlace()
            arguments.hasBase() && arguments.hasGroup("in") -> placeItemInContainer(arguments)
            else -> display("Place what where? Try 'place <item> in <target>'.")
        }
    }

    private fun placeItemInContainer(args: Args) {
        val item = GameState.player.inventory.getItem(args.getBaseString())
        if (item != null) {
            val targetString = args.getString("in")
            val destinations = GameState.currentLocation().getTargets(targetString).filterUniqueByName()
            when {
                targetString.isNotBlank() && destinations.isEmpty() -> display("Couldn't find $targetString")
                destinations.size == 1 -> EventManager.postEvent(TransferItemEvent(GameState.player, item, GameState.player, destinations.first(), true))
                else -> giveToWhat(destinations, args.getBaseString())
            }
        } else {
            display("Couldn't find ${args.getBaseString()}")
        }
    }

    private fun clarifyItemToPlace() {
        val targets = GameState.player.inventory.getItems().map { it.name }
        val message = "Give what item?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.map { it to "place $it in" }.toMap()))
    }

    private fun giveToWhat(creatures: List<Target>, itemName: String) {
        val message = "Give $itemName to what?\n\t${creatures.joinToString(", ")}"
        val response = ResponseRequest(message, creatures.map { it.name to "give $itemName to ${it.name}" }.toMap())
         CommandParser.setResponseRequest(response)
    }


}
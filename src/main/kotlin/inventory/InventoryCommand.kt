package inventory

import core.GameState
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.target.Target

class InventoryCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Bag", "b", "backpack")
    }

    override fun getDescription(): String {
        return "Bag:\n\tView and manage your inventory."
    }

    override fun getManual(): String {
        return "\n\tBag - list items in your inventory." +
                "\n\tBag <target> - list items in the target's inventory, if possible."
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val location = GameState.currentLocation()
        val allInventories = location.findTargetsByTag("Container")
        val argString = args.joinToString(" ")
        val target = location.getTargets(argString).firstOrNull()

        when {
            args.isEmpty() && allInventories.size == 1 -> EventManager.postEvent(ListInventoryEvent(allInventories.first()))
            args.isEmpty() && keyword == "bag" -> clarifyTarget(allInventories)
            args.isEmpty() -> EventManager.postEvent(ListInventoryEvent())
            target != null -> EventManager.postEvent(ListInventoryEvent(target))
            else -> display("Could not find $argString")
        }
    }

    private fun clarifyTarget(targets: List<Target>) {
        val names = targets.map { it.name }
        val message = "View whose inventory?\n\t${names.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, names.map { it to "bag $it" }.toMap()))
    }


}
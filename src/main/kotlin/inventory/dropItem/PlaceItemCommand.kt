package inventory.dropItem

import core.commands.Args
import core.gameState.GameState
import core.gameState.getCreature
import core.history.display
import interact.scope.ScopeManager
import inventory.pickupItem.PickupItemEvent
import system.EventManager

class PlaceItemCommand : core.commands.Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Place", "Drop", "Give", "Put")
    }

    override fun getDescription(): String {
        return "Place:\n\tPlace an item from your inventory in another inventory or on the ground."
    }

    override fun getManual(): String {
        return "\n\tDrop <item> - Drop an item an item from your inventory." +
                "\n\tPlace <item> in <target> - Drop an item an item from your inventory. "
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (args.isNotEmpty()) {
            placeItem(args)
        } else {
            display("Drop what?")
        }
    }

    private fun placeItem(arguments: List<String>) {
        val args = Args(arguments, delimiters = listOf("to", "in"))
        when {
            args.argStrings.size == 1 -> dropItem(args)
            args.argStrings.size == 2 -> placeItemInContainer(args)
            else -> display("Place what where? Try 'drop <item>' or 'place <item> in <target>'.")
        }
    }

    private fun dropItem(args: Args) {
        val item = GameState.player.creature.inventory.getItem(args.argStrings[0])
        if (item != null) {
            EventManager.postEvent(PlaceItemEvent(GameState.player.creature, item))
        } else {
            display("Couldn't find ${args.argStrings[0]}")
        }
    }

    private fun placeItemInContainer(args: Args) {
        val item = GameState.player.creature.inventory.getItem(args.argStrings[0])
        if (item != null) {
            val destination = ScopeManager.getScope().getTarget(args.argStrings[1])?.getCreature()
            if (destination != null) {
                EventManager.postEvent(PlaceItemEvent(GameState.player.creature, item, destination, true))
            } else {
                display("Couldn't find ${args.argStrings[1]}")
            }
        } else {
            display("Couldn't find ${args.argStrings[1]}")
        }
    }

}
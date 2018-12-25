package inventory.pickupItem

import core.gameState.GameState
import core.gameState.Item
import core.history.display
import interact.scope.ScopeManager
import system.EventManager

class PickupItemCommand : core.commands.Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Pickup", "p", "get", "add", "take")
    }

    override fun getDescription(): String {
        return "Pickup:\n\tAdd an item to your inventory"
    }

    override fun getManual(): String {
        return "\n\tPickup <item> - pickup an item"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (args.isNotEmpty()) {
            pickupItem(args)
        } else {
            display("Pickup what?")
        }
    }

    private fun pickupItem(itemArgs: List<String>) {
        if (ScopeManager.getScope().targetExists(itemArgs) && ScopeManager.getScope().getTarget(itemArgs) is Item) {
            val item = ScopeManager.getScope().getTarget(itemArgs) as Item
            EventManager.postEvent(PickupItemEvent(GameState.player.creature, item))
        } else {
            display("Couldn't find ${itemArgs.joinToString(" ")}")
        }
    }
}
package crafting.craft

import core.events.EventListener
import core.GameState
import inventory.Inventory
import core.target.Target
import core.history.display
import core.utility.StringFormatter
import inventory.dropItem.TransferItemEvent
import core.events.EventManager
import core.target.item.ItemManager
import crafting.DiscoverRecipeEvent

class Craft : EventListener<CraftRecipeEvent>() {

    override fun execute(event: CraftRecipeEvent) {
        when {
            event.tool?.isWithinRangeOf(event.source) == false -> display(StringFormatter.getSubject(event.source) + " " + StringFormatter.getIsAre(event.source) + " too far away to use ${event.tool}.")
            event.recipe.canBeCraftedBy(event.source, event.tool) -> {
                val ingredients = event.recipe.getUsedIngredients(event.source.inventory.getAllItems())
                val results = event.recipe.getResults(ingredients)
                removeIngredients(event.source.inventory, ingredients)
                addResults(results, event)
                EventManager.postEvent(DiscoverRecipeEvent(event.source, event.recipe))
//            TODO - Add XP
                display("You ${event.recipe.craftVerb} ${ingredients.joinToString(", ") { it.name }} and get ${results.joinToString(", ") { ItemManager.getTaggedItemName(it) }}.")
            }
            else -> display("You aren't able to craft ${event.recipe.name}.")
        }
    }

    private fun removeIngredients(inventory: Inventory, ingredients: List<Target>) {
        ingredients.forEach {
            inventory.remove(it)
        }
    }

    private fun addResults(results: List<Target>, event: CraftRecipeEvent) {
        results.forEach {
            EventManager.postEvent(TransferItemEvent(GameState.player, it, destination = event.source, silent = true))
        }
    }

}
package crafting

import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import inventory.pickupItem.PickupItemEvent
import system.EventManager
import system.ItemManager

class Cook : EventListener<CookAttemptEvent>() {

    override fun execute(event: CookAttemptEvent) {
        if (!crafterHasIngredients(event)) {
            println("You don't have all of those ingredients.")
        } else {
            val recipe = RecipeManager.findRecipe(event.ingredients, event.tool, event.source.soul)
            if (recipe == null) {
                //Maybe still consume the items?
                println("Nothing happens")
            } else {
                if (ItemManager.itemExists(recipe.result)){
                    removeIngredients(event)
                    EventManager.postEvent(PickupItemEvent(event.source, ItemManager.getItem(recipe.result), true))
                    discoverRecipe(event.source, recipe)
                    //TODO - Add XP
                    println("You cook ${event.ingredients.joinToString(",")} and get a ${recipe.result}")
                } else {
                    println("Seems like ${recipe.result} doesn't exist.")
                }

            }
        }
    }

    private fun crafterHasIngredients(event: CookAttemptEvent): Boolean {
        event.ingredients.forEach {
            if (!event.source.inventory.exists(it.name)) {
                return false
            }
        }
        return true
    }

    private fun removeIngredients(event: CookAttemptEvent) {
        event.ingredients.forEach {
            event.source.inventory.remove(event.source.inventory.getItem(it.name))
        }
    }

    private fun discoverRecipe(source: Creature, recipe: Recipe) {
        if (source == GameState.player.creature){
            if (!GameState.player.knownRecipes.contains(recipe)){
                GameState.player.knownRecipes.add(recipe)
            }
        }
    }


}
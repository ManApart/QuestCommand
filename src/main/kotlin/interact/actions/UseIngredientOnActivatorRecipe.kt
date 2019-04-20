package interact.actions

import core.events.EventListener
import core.history.display
import crafting.CraftRecipeEvent
import crafting.RecipeManager
import interact.UseEvent
import system.EventManager

class UseIngredientOnActivatorRecipe : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return if (event.used.properties.isItem() && event.target.properties.isActivator()){
            RecipeManager.findCraftableRecipes(listOf(event.used), event.target, event.source.soul).isNotEmpty()
        } else {
            false
        }
    }

    override fun execute(event: UseEvent) {
        val recipes = RecipeManager.findCraftableRecipes(listOf(event.used), event.target, event.source.soul)

        when {
            recipes.size > 1 -> display("What do you want to craft? ${recipes.joinToString(" or ") { it.name }}")
            else -> EventManager.postEvent(CraftRecipeEvent(event.source, recipes.first(), event.target ))
        }
    }
}
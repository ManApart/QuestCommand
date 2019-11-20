package crafting

import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import interact.scope.ScopeManager
import system.EventManager
import system.item.ItemManager

class CookCommand : Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("Cook", "Bake")
    }

    override fun getDescription(): String {
        return "Cook:\n\tCook food"
    }

    override fun getManual(): String {
        return "\n\tCook <ingredient>, <ingredient2> on <range> - Cook one or more ingredients on a range."
    }

    override fun getCategory(): List<String> {
        return listOf("Crafting")
    }

    override fun execute(keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(","), ArgDelimiter(listOf("with", "on")))
        val arguments = Args(args, delimiters)
        if (!isValidInput(arguments)) {
            display("Make sure to separate ingredients with commas, and then specify what tool you're using by saying on <tool>")
        } else {
            val ingredients = getIngredients(arguments)
            val tool = getTool(arguments)
            val recipes = RecipeManager.findCraftableRecipes(ingredients, tool, GameState.player.soul)

            when {
                tool == null -> display("Couldn't find something to cook on")
                ingredients.size != arguments.getBaseAndStrings(",").size -> display("Couldn't understand all of the ingredients. Found: ${ingredients.joinToString { it.name + ", " }}")
                recipes.isEmpty() -> display("Couldn't find a recipe for those ingredients")
                recipes.size > 1 -> display("What do you want to craft? ${recipes.joinToString(" or ") { it.name }}")
                else -> EventManager.postEvent(CraftRecipeEvent(GameState.player, recipes.first(), tool))
            }
        }
    }

    private fun isValidInput(args: Args): Boolean {
        return args.hasBase() && args.hasGroup("on")
    }

    private fun getIngredients(args: Args): List<Target> {
        val ingredients = mutableListOf<Target>()
        args.getBaseAndStrings(",").forEach {
            if (ItemManager.itemExists(it)) {
                ingredients.add(ItemManager.getItem(it))
            }
        }
        return ingredients
    }

    private fun getTool(args: Args): Target? {
        val group = args.getGroup("on")
        val scope = ScopeManager.getScope()
        return (scope.getActivators(group.joinToString(" ")) + scope.findActivatorsByTag("Range")).firstOrNull()
    }

}
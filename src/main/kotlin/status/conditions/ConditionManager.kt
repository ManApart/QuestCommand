package status.conditions

import core.DependencyInjector
import core.body.BodyPart
import core.utility.NameSearchableList
import status.effects.Effect
import status.effects.EffectManager
import status.effects.EffectRecipe

object ConditionManager {
    private var conditionRecipes = loadConditions()

    fun reset() {
        conditionRecipes = loadConditions()
    }

    private fun loadConditions(): NameSearchableList<ConditionRecipe> {
        val parser = DependencyInjector.getImplementation(ConditionParser::class.java)
        return parser.loadConditions()
    }

    fun getCondition(name: String, bodyParts: List<BodyPart>): Condition {
        val recipe = conditionRecipes.get(name)
        return getCondition(recipe, bodyParts)
    }

    fun getCondition(recipe: ConditionRecipe, bodyParts: List<BodyPart>): Condition {
        val effects = recipe.effects.map { buildEffect(it, bodyParts) }
        val criticalEffects = recipe.criticalEffects.map { buildEffect(it, bodyParts) }
        return Condition(recipe.name, recipe.element, recipe.elementStrength, effects, criticalEffects, recipe.permanent)
    }

    private fun buildEffect(recipe: EffectRecipe, bodyParts: List<BodyPart>): Effect {
        return EffectManager.getEffect(recipe.name, recipe.amount, recipe.duration, bodyParts)
    }


}
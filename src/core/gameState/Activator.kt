package core.gameState

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.behavior.BehaviorRecipe
import core.gameState.climb.Climbable
import core.gameState.inhertiable.InheritRecipe
import system.BehaviorManager
import system.InheritableManager

class Activator(name: String, description: String, val climb: Climbable?, @JsonProperty("behaviors") private val behaviorRecipes: MutableList<BehaviorRecipe> = mutableListOf(), properties: Properties = Properties(), inherits: List<InheritRecipe> = listOf()) : Target {
    val creature = Creature(name, description, parent = this, properties = properties)
    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val properties: Properties get() = creature.properties

    init {
        applyInherits(inherits)
    }

    val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)

    init {
        properties.tags.remove("Creature")
    }

    private fun applyInherits(inherits: List<InheritRecipe>) {
        inherits.forEach {
            val inherit = InheritableManager.getInheritable(it)
            val behaviorRecipeNames = behaviorRecipes.map { it.name }
            behaviorRecipes.addAll(inherit.behaviorRecipes.filter { !behaviorRecipeNames.contains(it.name) })
            properties.inherit(inherit.properties)
        }
    }

    override fun toString(): String {
        return name
    }

    fun evaluateAndExecute(event: Event) {
        behaviors.forEach { it.evaluateAndExecute(this, event) }
    }

}


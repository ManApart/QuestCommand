package crafting

import org.testng.annotations.Test
import org.testng.Assert.assertEquals

class RecipeResultTest {

    @Test(expected = IllegalArgumentException::class)
    fun recipeErrorsIfNeitherNameNorIDIsGiven() {
        RecipeResult()
    }

    @Test(expected = IllegalArgumentException::class)
    fun recipeErrorsIfNameAndIDAreBlank() {
        RecipeResult(" ", null)
    }

    @Test
    fun noErrorWithName() {
        val recipe = RecipeResult("Ingredient")
        assertEquals("Ingredient", recipe.name)
    }

    @Test
    fun noErrorWithId() {
        val recipe = RecipeResult(id = 0)
        assertEquals(0, recipe.id)
    }
}
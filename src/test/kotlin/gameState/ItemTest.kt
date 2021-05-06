package gameState

import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.properties.Values
import org.testng.annotations.Test
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue

class ItemTest {

    @Test
    fun canBeHeldByContainerWithProperties() {
        val item = Target("Apple", properties = Properties(tags = Tags(listOf("Raw"))))
        val properties = Properties(values = Values(mapOf("CanHold" to "Raw,Food")))
        assertTrue(item.properties.canBeHeldByContainerWithProperties(properties))
    }

    @Test
    fun canBeHeldByContainerWithPropertiesEmpty() {
        val item = Target("Apple")
        val properties = Properties()
        assertTrue(item.properties.canBeHeldByContainerWithProperties(properties))
    }

    @Test
    fun canBeHeldByContainerWithPropertiesNegative() {
        val item = Target("Apple", properties = Properties(tags = Tags(listOf("Small"))))
        val properties = Properties(values = Values(mapOf("CanHold" to "Raw,Food")))
        assertFalse(item.properties.canBeHeldByContainerWithProperties(properties))
    }
}
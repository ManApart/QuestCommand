package core.utility

import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class NameSearchableListAnyTest {

    @Test
    fun getAnyUnique() {
        val targetA = NamedString("ItemA")
        val targetB = NamedString("ItemB")
        val targetC = NamedString("ItemC")

        val list = NameSearchableList<NamedString>()
        list.add(targetA)
        list.add(targetB)
        list.add(targetC)

        val results = list.getAny(listOf("ItemA", "itemb"))

        assertEquals(2, results.size)
        assertTrue(results.contains(targetA))
        assertTrue(results.contains(targetB))
    }

    @Test
    fun getAnyOverlapReturnsAsManyAsMatch() {
        val targetA = NamedString("Left Hand")
        val targetB = NamedString("Right Hand")
        val targetC = NamedString("Chest")

        val list = NameSearchableList<NamedString>()
        list.add(targetA)
        list.add(targetB)
        list.add(targetC)

        val results = list.getAny(listOf("hand"))

        assertEquals(2, results.size)
        assertTrue(results.contains(targetA))
        assertTrue(results.contains(targetB))
    }

    @Test
    fun getAnyOverlapPrefersGroupedArgs() {
        val targetA = NamedString("Left Hand")
        val targetB = NamedString("Right Hand")
        val targetC = NamedString("Chest")

        val list = NameSearchableList<NamedString>()
        list.add(targetA)
        list.add(targetB)
        list.add(targetC)

        val results = list.getAny(listOf("left", "hand", "chest"))

        assertEquals(2, results.size)
        assertTrue(results.contains(targetA))
        assertTrue(results.contains(targetC))
    }

    @Test
    fun getAnyOverlapPrefersGroupedArgs2() {
        val targetA = NamedString("Left Hand")
        val targetB = NamedString("Right Hand")
        val targetC = NamedString("hand")

        val list = NameSearchableList<NamedString>()
        list.add(targetA)
        list.add(targetB)
        list.add(targetC)

        val results = list.getAny(listOf("left", "hand", "right hand"))

        assertEquals(2, results.size)
        assertTrue(results.contains(targetA))
        assertTrue(results.contains(targetB))
    }
}
import core.utility.Named
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue

fun assertEqualsByName(expected: Named, actual: Named) {
    assertEquals(expected.name, actual.name)
}

fun assertContainsByName(list: Collection<Named>, expected: Named) {
    val names = list.map { it.name }
    assertTrue(names.contains(expected.name), "${expected.name} was not found in $names")
}
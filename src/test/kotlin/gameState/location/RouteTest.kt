package gameState.location

import core.gameState.Direction
import core.gameState.Position
import core.gameState.location.LocationLink
import core.gameState.location.LocationNode
import core.gameState.location.Route
import org.junit.Test
import kotlin.test.assertEquals

class RouteTest {

    @Test
    fun addLink() {
        val source = LocationNode("source")
        val destination = LocationNode("destination")
        val route = Route(source)
        route.addLink(LocationLink(source, destination))

        assertEquals(1, route.getLinks().size)
        assertEquals(source, route.source)
        assertEquals(destination, route.destination)
    }

    @Test(expected = IllegalArgumentException::class)
    fun routeDoesNotAcceptLinksOutOfOrder() {
        val source = LocationNode("source")
        val destination = LocationNode("destination")
        val route = Route(source)
        route.addLink(LocationLink(destination, source))
    }

    @Test
    fun routeDirectionString() {
        val route = Route(LocationNode("source"))
        val directions = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST)

        directions.forEach {
            route.addLink(LocationLink(route.destination, LocationNode(it.name), Position.fromDirection(it)))
        }

        assertEquals("N, NE, NW, E, W, S, SW, SE", route.getDirectionString())
    }

}
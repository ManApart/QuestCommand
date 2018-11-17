package core.gameState.location

import core.gameState.Direction
import core.utility.Named
import system.location.LocationManager

class LocationNode(override val name: String, private val locationName: String = name, val parent: String? = null, private val locations: MutableList<LocationLink> = mutableListOf()) : Named {

    override fun toString(): String {
        return name
    }

    fun getDescription(): String {
        return name
    }

    fun addLink(link: LocationLink) {
        if (!hasLink(link)) {
            locations.add(link)
        }
    }

    private fun hasLink(link: LocationLink): Boolean {
        return locations.any { it.name == link.name }
    }

    fun getNeighborLinks(): List<LocationLink> {
        return locations.toList()
    }

    fun getNeighbors(): List<LocationNode> {
        return locations.map {
            LocationManager.getLocationNode(it.name)
        }
    }

    fun getNeighbors(direction: Direction): List<LocationNode> {
        val links = locations.filter {
            it.position.getDirection() == direction
        }
        return links.map {
            LocationManager.getLocationNode(it.name)
        }
    }

    fun getLocation(): Location {
        return LocationManager.getLocation(locationName)
    }

    fun nameMatches(args: List<String>): Boolean {
        return name.toLowerCase().split(" ").contains(args[0])
    }

    fun getLink(destination: LocationNode): LocationLink {
        return locations.first { it.name == destination.name }
    }

//    fun getNearestNeighbor(direction: Direction): LocationNode {
//
//    }

//    fun getPathTo(destination: LocationNode): List<LocationNode> {
//
//    }
//
//    fun getDistanceTo(destination: LocationNode): List<LocationNode> {
//
//    }
//
//    private fun getDistanceOfPath(path: List<LocationNode>) : Int {
//
//    }


}
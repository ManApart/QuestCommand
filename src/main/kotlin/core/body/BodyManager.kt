package core.body

import traveling.location.location.LocationRecipe
import traveling.location.location.LocationNode
import traveling.location.Network
import core.utility.NameSearchableList
import core.DependencyInjector
import traveling.location.location.LocationHelper
import traveling.location.location.LocationParser

object BodyManager {
    private const val bodiesPath = "/data/generated/content/bodies/bodies"
    private const val bodyPartPath = "/data/generated/content/bodies/parts"
    private val locationHelper = LocationHelper()
    private var parser = DependencyInjector.getImplementation(LocationParser::class.java)
    private var bodies = createBodies()

    fun reset() {
        parser = DependencyInjector.getImplementation(LocationParser::class.java)
        bodies = createBodies()
    }

    private fun createBodies(): NameSearchableList<Body> {
        val nodes: List<LocationNode> = parser.loadLocationNodes(bodiesPath)
        val bodyParts = parser.loadLocations(bodyPartPath)

        val nodeMap = locationHelper.buildInitialMap(nodes)
        locationHelper.createNeighborsAndNeighborLinks(nodeMap)
        val locations = createLocations(bodyParts, nodeMap.values.flatten())

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }.distinct()
            val network = Network(entry.key, entry.value, networkLocations)
            entry.value.forEach { it.network = network }
            network
        }

        val bodies = (networks.map { Body(it.name, it) }).plus(NONE)

        return NameSearchableList(bodies)
    }

    private fun createLocations(bodyParts: List<LocationRecipe>, locationNodes: List<LocationNode>) : NameSearchableList<LocationRecipe> {
        val locations = NameSearchableList(bodyParts)

        locationNodes.forEach { node ->
            if (locations.getOrNull(node.locationName) == null){
                locations.add(LocationRecipe(node.locationName))
            }
        }

        return locations
    }

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.lowercase() == name.lowercase() } != null
    }

    fun getBody(name: String): Body {
        return Body(bodies.get(name))
    }
}
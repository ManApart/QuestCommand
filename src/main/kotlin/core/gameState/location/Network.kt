package core.gameState.location

import core.utility.NameSearchableList
import core.utility.Named

class Network(override val name: String, locationNodes: List<LocationNode> = listOf(), locations: List<Location> = listOf()) : Named{
    private val locationNodes = NameSearchableList(locationNodes)
    private val locations = NameSearchableList(locations)

    fun getLocationNode(name: String): LocationNode {
        return locationNodes.get(name)
    }

    fun getLocationNodes() : List<LocationNode> {
        return locationNodes.toList()
    }

    fun findLocation(name: String): LocationNode {
        return when {
            locationNodes.exists(name) -> locationNodes.get(name)
            name.startsWith("$") -> NOWHERE_NODE
            else -> {
                println("Could not find location: $name")
                NOWHERE_NODE
            }
        }
    }

    fun countLocationNodes(): Int {
        return locationNodes.size
    }

    fun getLocation(name: String): Location {
        return locations.getOrNull(name) ?: NOWHERE
    }

    fun getLocations(): List<Location> {
        return locations.toList()
    }

    fun locationExists(name: String): Boolean {
        return locations.exists(name)
    }


    //    //TODO - test
//    fun findLeastDistant(locations: List<LocationNode>) : LocationNode {
//        return locations.sortedBy { position.getDistance(it.position) }.first()
//    }
//

//
//    private fun findOverlap(name: String, args: List<String>): Int {
//        var wordCount = 0
//        var remainingWords = name.toLowerCase()
//        for (i in 0 until args.size) {
//            when {
//                remainingWords.isBlank() -> return wordCount
//                remainingWords.contains(args[i]) -> {
//                    remainingWords = remainingWords.substring(remainingWords.indexOf(args[i]))
//                    wordCount++
//                }
//                else -> return wordCount
//            }
//        }
//
//        return wordCount
//    }

}
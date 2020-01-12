package core.target

import core.ai.behavior.BehaviorRecipe
import core.properties.getPersisted
import status.ProtoSoul
import traveling.location.location.DEFAULT_NETWORK
import traveling.location.location.LocationManager
import traveling.location.location.NOWHERE_NODE


fun getPersisted(dataObject: Target): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["aiName"] = dataObject.ai.name
    data["behaviorRecipes"] = dataObject.behaviorRecipes.map { it.name }
    data["body"] = core.body.getPersisted(dataObject.body)
    data["equipSlots"] = dataObject.equipSlots.map { it.attachPoints }
    data["description"] = dialogue.getPersisted(dataObject.getDynamicDescription2())
    data["inventory"] = inventory.getPersisted(dataObject.inventory)
    data["location"] = mapOf("network" to dataObject.location.network.name, "node" to dataObject.location.name)
    data["soul"] = status.getPersisted(dataObject.soul)
    data["properties"] = getPersisted(dataObject.properties)
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): Target {
    val name = data["name"] as String
    val aiName = data["aiName"] as String
    val behaviorRecipes = (data["behaviorRecipes"] as List<String>).map { BehaviorRecipe(it) }.toMutableList()
    val equipSlots = (data["equipSlots"] as List<List<String>>)
    val dynamicDescription = dialogue.readFromData(data["description"] as Map<String, Any>)
    val inventory = inventory.readFromData(data["inventory"] as Map<String, Any>)
    val body = core.body.readFromData(data["body"] as Map<String, Any>, inventory)
    val locationMap = (data["location"] as Map<String, String>)
    val location = LocationManager.getNetwork(locationMap["network"] ?: DEFAULT_NETWORK.name).getLocationNode(locationMap["node"] ?: NOWHERE_NODE.name)
    val props = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val target = Target(name, null, mapOf(), null, aiName, behaviorRecipes, body, null, equipSlots, dynamicDescription, listOf(), location, null, ProtoSoul(), props)
    target.inventory.addAll(inventory.getAllItems())
    status.readFromData(data["soul"] as Map<String, Any>, target.soul)

    return target
}

fun getKey(data: Map<String, Any>, key: String) : Map<String, Any> {
    return if (data.containsKey(key)){
        @Suppress("UNCHECKED_CAST")
        (data[key] as Map<String, Any>)
    } else {
        mapOf()
    }
}

fun getListKey(data: Map<String, Any>, key: String) : List<Map<String, Any>> {
    return if (data.containsKey(key)){
        @Suppress("UNCHECKED_CAST")
        (data[key] as List<Map<String, Any>>)
    } else {
        listOf()
    }
}
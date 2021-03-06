package core.target.item

import core.DependencyInjector
import core.target.Target
import traveling.location.location.LocationTarget

const val ITEM_TAG = "Item"

object ItemManager {
    private var parser = DependencyInjector.getImplementation(ItemParser::class.java)
    private var items = parser.loadItems()

    fun reset() {
        parser = DependencyInjector.getImplementation(ItemParser::class.java)
        items = parser.loadItems()
        items.map { it.properties.tags.add(ITEM_TAG) }
    }

    fun itemExists(name: String): Boolean {
        return items.exists(name)
    }

    fun getItem(name: String): Target {
        return Target(name, items.get(name))
    }

    fun getItems(names: List<String>): List<Target> {
        return names.map { getItem(it) }
    }

    fun getItemsFromLocationTargets(targets: List<LocationTarget>): List<Target> {
        return targets.map {
            val item = Target(it.name, items.get(it.name), it.params)
            if (!it.location.isNullOrBlank()) {
                item.properties.values.put("locationDescription", it.location)
            }
            item.position = it.position
            item
        }
    }

    fun getTaggedItemName(item: Target): String {
        val orig = getItem(item.name)
        val newTags = item.properties.tags.getAll() - orig.properties.tags.getAll()
        return if (newTags.isNotEmpty()) {
            newTags.joinToString(" ") + " " + item.name
        } else {
            item.name
        }
    }

}
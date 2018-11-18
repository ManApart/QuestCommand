package interact

import core.events.EventListener
import core.gameState.*
import core.gameState.Target
import core.history.display
import core.utility.NameSearchableList
import core.utility.StringFormatter
import inventory.pickupItem.ItemPickedUpEvent
import system.*
import travel.ArriveEvent

object ScopeManager {
    private val targets = NameSearchableList<Target>()

    init {
        resetTargets()
    }

    class ArrivalHandler : EventListener<ArriveEvent>() {
        override fun execute(event: ArriveEvent) {
            resetTargets()
            val location = event.destination.getLocation()
            addTargets(ItemManager.getItems(location.items))
            addTargets(ActivatorManager.getActivators(location.activators))
            addTargets(CreatureManager.getCreatures(location.creatures))
        }
    }

    class ItemSpawner : EventListener<ItemSpawnedEvent>() {
        override fun execute(event: ItemSpawnedEvent) {
            if (event.target == null) {
                val name = StringFormatter.format(event.item.count > 1, "${event.item.count}x ${event.item.name}s", event.item.name)
                display("$name appeared.")
                addTarget(event.item)
            } else {
                event.target.inventory.add(event.item)
                EventManager.postEvent(ItemPickedUpEvent(event.target, event.item))
            }
        }
    }

    //TODO - set location on spawn
    class ActivatorSpawner : EventListener<SpawnActivatorEvent>() {
        override fun execute(event: SpawnActivatorEvent) {
            display("${event.activator.name} appeared.")
            addTarget(event.activator)
        }
    }

    class ScopeRemover : EventListener<RemoveScopeEvent>() {
        override fun execute(event: RemoveScopeEvent) {
            if (targetExists(event.target)){
                removeTarget(event.target)
            } else if (event.target is Item) {
                getAllInventories().forEach {
                    if (it.exists(event.target)){
                        it.remove(event.target)
                        return
                    }
                }
            }
        }
    }

    fun addTarget(target: Target, proxies: List<String> = listOf()) {
        if (!targets.contains(target)) {
            targets.add(target)
        }
        if (proxies.isNotEmpty()){
            targets.addProxy(target, proxies)
        }
    }

    fun addTargets(targets: List<Target>) {
        ScopeManager.targets.addAll(targets)
    }

    fun removeTarget(target: Target) {
        targets.remove(target)
    }

    fun getTargets(): List<Target> {
        return targets.toList()
    }

    private fun resetTargets() {
        targets.clear()
        addTarget(GameState.player, listOf("me", "self"))
    }

    fun targetExists(name: String): Boolean {
        return targets.exists(name)
    }

    fun targetExists(name: List<String>): Boolean {
        return targets.exists(name)
    }

    fun targetExists(target: Target): Boolean {
        return targets.exists(target)
    }

    fun creatureExists(name: List<String>): Boolean {
        return targets.exists(name) && core.gameState.getCreature(getTarget(name)) != null
    }

    fun activatorExists(name: List<String>): Boolean {
        return targets.exists(name) && getTarget(name) is Activator
    }

    fun getTarget(name: String): Target {
        return targets.get(name)
    }

    fun getTarget(name: List<String>): Target {
        return targets.get(name)
    }

    fun getCreature(name: List<String>): Creature {
        return getCreature(targets.get(name))!!
    }

    fun getActivator(name: List<String>): Activator {
        return targets.get(name) as Activator
    }

    fun findTargetByTag(tag: String) : Target?{
        return targets.firstOrNull { it.properties.tags.has(tag) }
    }

    fun findActivatorByTag(tag: String) : Activator?{
        return targets.firstOrNull { it is Activator && it.properties.tags.has(tag) } as Activator
    }

    fun getAllSouls(): List<Soul> {
        val souls = mutableListOf<Soul>()
        souls.add(GameState.player.creature.soul)
        souls.addAll(GameState.player.creature.inventory.getAllItems().map { it.soul })

        getTargets().forEach { target ->
            if (target is Activator) {
                souls.add(target.creature.soul)
            } else if (target is Creature) {
                souls.add(target.soul)
                souls.addAll(target.inventory.getAllItems().map { item -> item.soul })
            }
        }
        return souls.toList()
    }

    fun getAllInventories(): List<Inventory> {
        val inventories = mutableListOf<Inventory>()
        inventories.add(GameState.player.creature.inventory)

        getTargets().forEach {
            if (it is Activator) {
                inventories.add(it.creature.inventory)
            } else if (it is Creature) {
                inventories.add(it.inventory)
            }
        }
        return inventories.toList()
    }

}
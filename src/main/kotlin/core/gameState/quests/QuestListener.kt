package core.gameState.quests

import core.events.Event
import core.events.EventListener
import core.utility.ReflectionTools

class QuestListener : EventListener<Event>() {
    private val listeners = mutableMapOf<Class<*>, MutableList<Quest>>()

    fun getListeners() : Map<Class<*>, List<Quest>> {
        return listeners
    }

    override fun getPriority() : Int {
        return 100
    }

    override fun execute(event: Event) {
        if (listeners.isEmpty()) {
            buildListeners()
        }
        if (listeners.containsKey(event.javaClass)) {
            val quests = listeners[event.javaClass]?.toList()
            quests?.forEach { quest ->
                val stage = quest.getMatchingEvent(event)
                if (stage != null) {
                    removeListeners(quest)
                    quest.executeEvent(stage)
                    if (!quest.complete) {
                        addListeners(quest)
                    }
                }
            }
        }
    }

    fun reset() {
        listeners.clear()
        buildListeners()
    }

    private fun removeListeners(quest: Quest) {
        quest.getListenedForEvents().forEach { event ->
            val clazz = ReflectionTools.getEvent(event.condition.callingEvent)
            listeners[clazz]?.remove(quest)
            if (listeners[clazz]?.isEmpty() == true){
                listeners.remove(clazz)
            }
        }
    }

    private fun buildListeners() {
        QuestManager.quests.forEach {
            addListeners(it)
        }
    }

    private fun addListeners(quest: Quest) {
        quest.getListenedForEvents().forEach { event ->
            val clazz = ReflectionTools.getEvent(event.condition.callingEvent)
            if (!listeners.containsKey(clazz)) {
                listeners[clazz] = mutableListOf()
            }
            listeners[clazz]?.add(quest)
        }
    }

}
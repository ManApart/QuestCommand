package processing

import events.EventListener
import events.UseItemEvent
import processing.use.Use
import utility.ReflectionTools

object UseManager {
    private val uses = loadUses()

    private fun loadUses(): List<Use> {
        return ReflectionTools.getAllUses().map { it.newInstance() }.toList()
    }

    class UseItemHandler() : EventListener<UseItemEvent>() {
        override fun handle(event: UseItemEvent) {
            println("You use ${event.source} on ${event.target}")
            val filteredUses = uses.filter { it.matches(event) }
            if (filteredUses.isEmpty()){
                println("Nothing happens.")
            } else {
                filteredUses.forEach { it.execute(event) }
            }
        }
    }
}
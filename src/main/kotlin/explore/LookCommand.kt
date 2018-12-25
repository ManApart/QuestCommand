package explore

import core.commands.Command
import core.history.display
import interact.scope.ScopeManager
import system.EventManager

class LookCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Look", "ls", "Examine", "Exa")
    }

    override fun getDescription(): String {
        return "Look:\n\tExamine your surroundings"
    }

    override fun getManual(): String {
        return "\n\tLook - View the objects you can interact with." +
                "\n\tLook <target> - Look at a specific target."
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            args.isEmpty() -> EventManager.postEvent(LookEvent())
            ScopeManager.getScope().targetExists(argString) -> EventManager.postEvent(LookEvent(ScopeManager.getScope().getTarget(argString)))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

}
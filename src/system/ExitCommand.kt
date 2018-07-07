package system

import core.commands.Command
import kotlin.system.exitProcess

class ExitCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Exit", "Quit", "qqq")
    }

    override fun getDescription(): String {
        return "Exit:\n\tExit the program."
    }

    override fun getManual(): String {
        return "\n\tExit - Exit the program."
    }

    override fun execute(args: List<String>) {
        //TODO - move to event / system manager
        println("Exiting")
        exitProcess(0)
    }
}
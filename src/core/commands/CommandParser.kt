package core.commands

import core.utility.ReflectionTools

object CommandParser {
    val commands = loadCommands()
    private val unknownCommand = UnknownCommand()
    private val filteredWords = listOf("to", "with", "on")

    private fun loadCommands(): List<Command> {
        return ReflectionTools.getAllCommands().map { it.newInstance() }.toList()
    }

    fun parseCommand(line: String) {
        val args: List<String> = cleanLine(line)
        if (args.isEmpty()) {
            unknownCommand.execute(listOf(line))
        } else {
            val command = findCommand(args[0])
            if (command == unknownCommand){
                unknownCommand.execute(listOf(line))
            } else {
                val trimmedArgs = removeFirstItem(args)
                command.execute(trimmedArgs)
            }
        }
    }

    private fun cleanLine(line: String): List<String> {
        var cleanedString = line.toLowerCase()
        filteredWords.forEach { cleanedString = cleanedString.replace(" $it ", " ") }
        return cleanedString.split(" ").map { it.trim() }.filter { it.isNotEmpty() }
    }

    fun findCommand(alias: String): Command {
        return commands.firstOrNull { containsIgnoreCase(it.getAliases(), alias) }?: unknownCommand
    }

    private fun containsIgnoreCase(textList: Array<String>, matchText: String): Boolean {
        val match = matchText.toLowerCase().trim()
        textList.forEach {
            if (it.toLowerCase().trim() == match) {
                return true
            }
        }
        return false
    }

}

fun removeFirstItem(list: List<String>): List<String> {
    return if (list.size > 1) list.subList(1, list.size) else listOf()
}

fun removeFirstItem(list: Array<String>): Array<String> {
    return if (list.size > 1) list.toList().subList(1, list.size).toTypedArray() else arrayOf()
}
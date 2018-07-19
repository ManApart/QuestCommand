import core.commands.CommandParser
import system.EventManager
import system.GameManager

fun main(args: Array<String>) {
    GameManager.newGame()
    CommandParser.parseCommand("map")
    while (true){
        CommandParser.parseCommand(readLine() ?: "")
    }
}

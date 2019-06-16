package status.journal

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.quests.QuestManager
import core.history.display
import system.EventManager

class JournalCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Quest", "Quests", "Journal", "Q")
    }

    override fun getDescription(): String {
        return "Quest:\n\tView your active and completed Quests."
    }

    override fun getManual(): String {
        return "\n\tQuest active - View active Quests." +
                "\n\tQuest all - View all quests." +
                "\n\tQuest <quest> - View entries for a specific quest."
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override fun execute(keyword: String, args: List<String>) {
        when {
            args.isEmpty() && keyword == "Quest" -> clarifyQuest()

            args.isEmpty() -> EventManager.postEvent(ViewQuestListEvent(justActive = true))
            args.size == 1 && args[0] == "active" -> EventManager.postEvent(ViewQuestListEvent(justActive = true))

            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ViewQuestListEvent(justActive = false))

            args.size == 1 && args[0] == "quest" -> clarifyWhichQuest()

            else -> {
                val quest = QuestManager.quests.getOrNull(args.joinToString())
                if (quest != null) {
                    EventManager.postEvent(ViewQuestJournalEvent(quest))
                } else {
                    display("Couldn't find quest: ${args.joinToString(" ")}")
                }
            }
        }
    }

    private fun clarifyQuest() {
        val targets = listOf("Active", "All", "Quest")
        display("Info about what type?\n\t${targets.joinToString(", ")}")
        CommandParser.responseRequest = ResponseRequest(targets.map { it to "quest $it" }.toMap())
    }

    private fun clarifyWhichQuest() {
        val targets = QuestManager.getAllPlayerQuests().map { it.name }
        display("Info about which quest?\n\t${targets.joinToString(", ")}")
        CommandParser.responseRequest = ResponseRequest(targets.map { it to "quest $it" }.toMap())
    }

}



package conversation

import conversation.input.Dialogue
import conversation.input.DialoguesCollection
import core.DependencyInjector

object ConversationManager {
    private var parser = DependencyInjector.getImplementation(DialoguesCollection::class.java)
    private var dialogues = parser.values

    fun reset() {
        parser = DependencyInjector.getImplementation(DialoguesCollection::class.java)
        dialogues = parser.values
    }

    fun getMatchingDialogue(conversation: Conversation): List<Dialogue> {
        return dialogues.filter { it.matches(conversation) }
    }
}
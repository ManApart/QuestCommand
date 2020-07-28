package conversation.dialogue

import conversation.QuestionType
import conversation.Verb
import core.events.Event
import core.target.Target
import core.utility.StringFormatter

class DialogueEvent(val speaker: Target, val listener: Target, val subject: Target, val verb: Verb, val verbOption: String?, val questionType: QuestionType = QuestionType.STATEMENT) : Event {
    fun print(): String {
        val verbOptionSuffix = StringFormatter.format(verbOption !=null, " $verbOption", "")
        return "${speaker.name}: ${questionType.name.toLowerCase().capitalize()} ${subject.name} ${verb.name.toLowerCase()}${verbOptionSuffix}?"
    }
}
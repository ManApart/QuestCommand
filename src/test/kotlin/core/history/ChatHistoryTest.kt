package core.history

import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class ChatHistoryTest {

    @BeforeClass
    fun setup(){
        ChatHistory.reset()
    }

    @Test
    fun beforeChatHistoryIsEmpty(){
        assertEquals(InputOutput().input, ChatHistory.getLastInput())
        assertEquals("", ChatHistory.getLastOutput())
        assertEquals(0, ChatHistory.history.size)
    }

    @Test
    fun displayAddsMessageToHistory(){
        val message = "Test Message"
        display(message)
        assertEquals(InputOutput().input, ChatHistory.getLastInput())
        assertEquals(message, ChatHistory.getLastOutput())
    }
}
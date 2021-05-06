
import core.DependencyInjector
import core.events.*
import org.testng.Assert.assertEquals
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class EventManagerTest {
    private val resultList = mutableListOf<String>()
    private val first = TestListener(this, 0, "first")
    private val second = TestListener(this, 1, "second")
    private val child = TestChildListener(this, 2, "child")

    class TestEvent : Event
    open class TestListener(private val parent: EventManagerTest, private val priorityLevel: Int, private val id: String) : EventListener<TestEvent>() {
        override fun getPriorityRank(): Int {
            return priorityLevel
        }

        override fun execute(event: TestEvent) {
            parent.resultList.add(id)
        }

    }

    class TestChildListener(parent: EventManagerTest, priorityLevel: Int, id: String) : TestListener(parent, priorityLevel, id)

    companion object {
        @BeforeClass
        @JvmStatic fun setupAll() {
            DependencyInjector.setImplementation(EventListenersCollection::class.java, EventListenersMock())
        }

        @AfterClass
        @JvmStatic fun teardown() {
            DependencyInjector.clearImplementation(EventListenersCollection::class.java)
        }
    }


    @Test
    fun higherPriorityRunsSooner(){
        EventManager.registerListener(first)
        EventManager.registerListener(second)

        EventManager.postEvent(TestEvent())
        EventManager.executeEvents()

        EventManager.unRegisterListener(first)
        EventManager.unRegisterListener(second)

        assertEquals("first", resultList[0])
        assertEquals("second", resultList[1])
        resultList.clear()
    }

    @Test
    fun higherPriorityRunsSoonerRegardlessOfAddOrder(){
        EventManager.registerListener(second)
        EventManager.registerListener(first)

        EventManager.postEvent(TestEvent())
        EventManager.executeEvents()

        EventManager.unRegisterListener(first)
        EventManager.unRegisterListener(second)

        assertEquals("first", resultList[0])
        assertEquals("second", resultList[1])
        resultList.clear()
    }

    @Test
    fun childClassStillFound(){
        EventManager.registerListener(child)

        EventManager.postEvent(TestEvent())
        EventManager.executeEvents()

        EventManager.unRegisterListener(child)

        assertEquals("child", resultList[0])
        resultList.clear()
    }

}
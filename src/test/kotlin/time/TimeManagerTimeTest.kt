package time

import org.junit.Test
import time.TimeManager
import kotlin.test.assertEquals

class TimeManagerTimeTest {

    @Test
    fun getPercentDayComplete0() {
        val time = TimeManager()
        assertEquals(0, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayComplete25() {
        val time = TimeManager(TimeManager.ticksInDay / 4)
        assertEquals(25, time.getPercentDayComplete())
    }
    @Test
    fun getPercentDayComplete50() {
        val time = TimeManager(TimeManager.ticksInDay / 2)
        assertEquals(50, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayComplete99() {
        val time = TimeManager(TimeManager.ticksInDay - 1)
        assertEquals(99, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayComplete100() {
        val time = TimeManager(TimeManager.ticksInDay)
        assertEquals(0, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayCompleteDay2() {
        val time = TimeManager(TimeManager.ticksInDay + 1)
        assertEquals(1, time.getPercentDayComplete())
    }

    @Test
    fun getDateAndTime() {
        val year = 2 * TimeManager.ticksInYear
        val month = 3 * TimeManager.ticksInMonth
        val day = 5 * TimeManager.ticksInDay
        val hour = 2 * TimeManager.ticksInHour

        val time = TimeManager(year + month + day + hour)

        assertEquals(2, time.getYear())
        assertEquals(3, time.getMonth())
        assertEquals(5, time.getDay())
        assertEquals(2, time.getHour())
    }

}
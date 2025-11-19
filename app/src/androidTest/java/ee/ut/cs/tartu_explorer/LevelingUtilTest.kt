package ee.ut.cs.tartu_explorer

import androidx.test.ext.junit.runners.AndroidJUnit4
import ee.ut.cs.tartu_explorer.core.util.LevelInfo
import ee.ut.cs.tartu_explorer.core.util.LevelingUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LevelingUtilTest {

    @Test
    fun testHintEp() {
        assertEquals(50, LevelingUtil.calculateEpForQuest(0))
        assertEquals(45, LevelingUtil.calculateEpForQuest(1))
        assertEquals(40, LevelingUtil.calculateEpForQuest(2))
        assertEquals(35, LevelingUtil.calculateEpForQuest(3))
        assertEquals(30, LevelingUtil.calculateEpForQuest(4))
        assertEquals(25, LevelingUtil.calculateEpForQuest(5))
        assertEquals(55, LevelingUtil.calculateEpForQuest(-1))
        assertEquals(100, LevelingUtil.calculateEpForQuest(-10))
        assertEquals(0, LevelingUtil.calculateEpForQuest(100))
    }

    @Test
    fun testLevelCalculation() {
        assertEquals(
            LevelInfo(1, 0.0f, 0, 100),
            LevelingUtil.calculateLevelInfo(0)
        )
        assertEquals(
            LevelInfo(1, 0.1f, 10, 100),
            LevelingUtil.calculateLevelInfo(10)
        )
        assertEquals(
            LevelInfo(2, 0.0f, 0, 182),
            LevelingUtil.calculateLevelInfo(100)
        )
        assertEquals(
            LevelInfo(3, 0.9198312f, 218, 237),
            LevelingUtil.calculateLevelInfo(500)
        )
        assertEquals(
            LevelInfo(5, 0.6289308f, 200, 318),
            LevelingUtil.calculateLevelInfo(1000)
        )
        assertEquals(
            LevelInfo(465, 0.15898547f, 514, 3233),
            LevelingUtil.calculateLevelInfo(1000000)
        )
        assertEquals(
            LevelInfo(1, -10.0f, -1000, 100),
            LevelingUtil.calculateLevelInfo(-1000)
        )
    }
}
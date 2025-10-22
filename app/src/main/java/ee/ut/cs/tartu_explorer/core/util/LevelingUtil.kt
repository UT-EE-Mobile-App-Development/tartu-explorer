package ee.ut.cs.tartu_explorer.core.util

import kotlin.math.pow

/**
 * Data class to hold all relevant information about a player's current level and progress.
 */
data class LevelInfo(
    val level: Int,
    val progressPercentage: Float, // Progress from current level to the next (0.0f to 1.0f)
    val currentLevelEp: Int,     // EP accumulated since the last level up
    val nextLevelThreshold: Int  // EP needed to reach the next level from the previous one
)

/**
 * A central utility object for all calculations and configurations related to the player leveling system.
 * This allows for easy balancing and modification of game mechanics in one place.
 */
object LevelingUtil {

    // --- Configuration ---
    private const val BASE_EP = 100.0
    private const val POWER = 1.5
    private const val EP_PER_QUEST = 50
    private const val HINT_PENALTY_PER_HINT = 5 // EP to subtract for each hint used

    /**
     * Calculates the experience points required to reach a specific level.
     * The formula implements a power curve, making later levels require more EP.
     *
     * @param level The target level.
     * @return The total EP required to have reached that level.
     */
    private fun getEpForLevel(level: Int): Int {
        if (level <= 1) return 0
        return (BASE_EP * (level - 1).toDouble().pow(POWER)).toInt()
    }

    /**
     * Calculates the player's current level and progress based on their total experience points.
     *
     * @param totalEp The player's total accumulated experience points.
     * @return A [LevelInfo] object containing detailed information for the UI.
     */
    fun calculateLevelInfo(totalEp: Int): LevelInfo {
        var level = 1
        while (totalEp >= getEpForLevel(level + 1)) {
            level++
        }

        val currentLevelThreshold = getEpForLevel(level)
        val nextLevelThreshold = getEpForLevel(level + 1)

        val epInCurrentLevel = totalEp - currentLevelThreshold
        val epForThisLevel = nextLevelThreshold - currentLevelThreshold

        val progress = if (epForThisLevel > 0) {
            epInCurrentLevel.toFloat() / epForThisLevel.toFloat()
        } else {
            0f // Avoid division by zero
        }

        return LevelInfo(
            level = level,
            progressPercentage = progress,
            currentLevelEp = epInCurrentLevel,
            nextLevelThreshold = epForThisLevel
        )
    }

    /**
     * Calculates the EP to be awarded for completing a quest, applying penalties for hint usage.
     *
     * @param hintsUsed The number of hints the player used for the quest.
     * @return The calculated experience points to award.
     */
    fun calculateEpForQuest(hintsUsed: Int): Int {
        val penalty = hintsUsed * HINT_PENALTY_PER_HINT
        val awardedEp = EP_PER_QUEST - penalty
        return awardedEp.coerceAtLeast(0) // Ensure EP doesn't go negative
    }
}

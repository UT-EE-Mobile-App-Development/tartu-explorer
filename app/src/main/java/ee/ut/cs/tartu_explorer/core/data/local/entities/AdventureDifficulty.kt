package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.TypeConverter

/**
 * Enum class representing the difficulty levels of an adventure.
 */
enum class AdventureDifficulty {
    VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD
}

/**
 * Converter class for converting AdventureDifficulty enum values to and from their
 * corresponding integer representations for database storage.
 */
class AdventureDifficultyConverter {

    /**
     * Converts an integer value to its corresponding AdventureDifficulty enum value.
     *
     * @param value The integer value representing the ordinal of the AdventureDifficulty.
     * @return The corresponding AdventureDifficulty enum value.
     */
    @TypeConverter
    fun toAdventureDifficulty(value: Int) = enumValues<AdventureDifficulty>()[value]

    /**
     * Converts an AdventureDifficulty enum value to its corresponding integer ordinal value.
     *
     * @param value The AdventureDifficulty enum value.
     * @return The integer ordinal value representing the AdventureDifficulty.
     */
    @TypeConverter
    fun fromAdventureDifficulty(value: AdventureDifficulty) = value.ordinal
}
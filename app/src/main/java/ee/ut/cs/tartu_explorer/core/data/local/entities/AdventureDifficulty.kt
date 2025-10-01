package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.TypeConverter


enum class AdventureDifficulty {
    VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD
}

class AdventureDifficultyConverter {

    @TypeConverter
    fun toAdventureDifficulty(value: Int) = enumValues<AdventureDifficulty>()[value]

    @TypeConverter
    fun fromAdventureDifficulty(value: AdventureDifficulty) = value.ordinal
}
package ee.ut.cs.tartu_explorer.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ee.ut.cs.tartu_explorer.core.data.MissionDifficulty

@Entity
data class Mission(
    val title: String,
    val difficulty: MissionDifficulty = MissionDifficulty.MEDIUM,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)


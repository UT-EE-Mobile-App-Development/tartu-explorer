package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map_quest")
data class MapQuestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val difficulty: Int, // 1-5 Schwierigkeitsgrad
    val estimatedDuration: Int, // in Minuten
    val thumbnailPath: String,
    val completed: Boolean = false
)

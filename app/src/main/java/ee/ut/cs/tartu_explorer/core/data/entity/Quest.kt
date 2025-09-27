package ee.ut.cs.tartu_explorer.core.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Mission::class,
        parentColumns = ["id"],
        childColumns = ["missionId"],
        onDelete = CASCADE
    )],
    indices = [Index(value = ["missionId"])]
)
data class Quest(
    val latitude: Double,
    val longitude: Double,
    val radius: Float = 25f,
    val missionId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

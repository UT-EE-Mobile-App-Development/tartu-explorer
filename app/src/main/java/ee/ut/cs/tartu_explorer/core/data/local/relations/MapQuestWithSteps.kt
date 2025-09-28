package ee.ut.cs.tartu_explorer.core.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import ee.ut.cs.tartu_explorer.core.data.local.entities.MapQuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestStepEntity

data class MapQuestWithSteps(
    @Embedded val quest: MapQuestEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questId"
    )
    val steps: List<QuestStepEntity>
)

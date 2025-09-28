package ee.ut.cs.tartu_explorer.core.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import ee.ut.cs.tartu_explorer.core.data.local.entities.MapQuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestStepEntity

/**
 * Represents a one-to-many relationship between a MapQuestEntity and its associated QuestStepEntity.
 * @property quest The MapQuestEntity associated with this relationship.
 * @property steps The list of QuestStepEntity associated with the quest.
 */
data class MapQuestWithSteps(
    @Embedded val quest: MapQuestEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questId"
    )
    val steps: List<QuestStepEntity>
)

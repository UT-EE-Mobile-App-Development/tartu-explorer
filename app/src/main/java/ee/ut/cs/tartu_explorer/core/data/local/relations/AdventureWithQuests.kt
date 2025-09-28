package ee.ut.cs.tartu_explorer.core.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity

/**
 * Represents a one-to-many relationship between a AdventureEntity and its associated QuestEntity.
 * @property quest The AdventureEntity associated with this relationship.
 * @property steps The list of QuestEntity associated with the quest.
 */
data class AdventureWithQuests(
    @Embedded val quest: AdventureEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "adventureId"
    )
    val steps: List<QuestEntity>
)

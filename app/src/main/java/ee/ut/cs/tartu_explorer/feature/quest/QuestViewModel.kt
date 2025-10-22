package ee.ut.cs.tartu_explorer.feature.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureRepository
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class QuestViewModel(
    adventureRepository: AdventureRepository,
    gameRepository: GameRepository
) : ViewModel() {

    private val adventuresFlow = adventureRepository.getAdventures()
    // Player ID is hardcoded for now, you can change this later
    private val statusDetailsFlow = gameRepository.getAdventureStatusDetails(1L)

    val state = combine(
        adventuresFlow,
        statusDetailsFlow
    ) { adventures, details ->
        val statuses = details.mapValues { it.value.status }
        QuestState(
            adventures = adventures.groupBy { it.difficulty },
            adventureStatuses = statuses,
            adventureStatusDetails = details
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestState())

}

class QuestViewModelFactory(
    private val adventureRepository: AdventureRepository,
    private val gameRepository: GameRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestViewModel(adventureRepository, gameRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

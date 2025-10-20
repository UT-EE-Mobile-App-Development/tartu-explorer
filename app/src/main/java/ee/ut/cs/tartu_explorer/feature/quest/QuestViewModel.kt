package ee.ut.cs.tartu_explorer.feature.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureRepository
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestViewModel(
    private val adventureRepository: AdventureRepository,
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _state = MutableStateFlow(QuestState())

    private var _adventures = adventureRepository
        .getAdventures()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = _state.combine(_adventures) { state: QuestState, adventures ->
        state.copy(
            adventures = mapOf(
                AdventureDifficulty.VERY_EASY to adventures.filter { it -> it.difficulty == AdventureDifficulty.VERY_EASY },
                AdventureDifficulty.EASY to adventures.filter { it -> it.difficulty == AdventureDifficulty.EASY },
                AdventureDifficulty.MEDIUM to adventures.filter { it -> it.difficulty == AdventureDifficulty.MEDIUM },
                AdventureDifficulty.HARD to adventures.filter { it -> it.difficulty == AdventureDifficulty.HARD },
                AdventureDifficulty.VERY_HARD to adventures.filter { it -> it.difficulty == AdventureDifficulty.VERY_HARD },
            ),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestState())

    init {
        viewModelScope.launch {
            // Assuming a logged-in user with ID 1L for now
            val statuses = gameRepository.getAdventureStatuses(1L)
            _state.value = _state.value.copy(adventureStatuses = statuses)
        }
    }
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

package ee.ut.cs.tartu_explorer.feature.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class QuestViewModel(
    private val repository: AdventureRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(QuestState())

    private var _adventures = repository
        .getAdventures()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = _state.combine(_adventures) { state: QuestState, adventures ->
        state.copy(
            adventures = mapOf(
                AdventureDifficulty.VERY_EASY to adventures.filter { it -> it.difficulty == AdventureDifficulty.VERY_HARD},
                AdventureDifficulty.EASY to adventures.filter { it -> it.difficulty == AdventureDifficulty.EASY},
                AdventureDifficulty.MEDIUM to adventures.filter { it -> it.difficulty == AdventureDifficulty.MEDIUM},
                AdventureDifficulty.HARD to adventures.filter { it -> it.difficulty == AdventureDifficulty.HARD},
                AdventureDifficulty.VERY_HARD to adventures.filter { it -> it.difficulty == AdventureDifficulty.VERY_HARD},
            ),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestState())

}

class QuestViewModelFactory(private val repository: AdventureRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

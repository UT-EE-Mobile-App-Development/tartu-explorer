package ee.ut.cs.tartu_explorer.feature.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class GameViewModel(
    private val adventureId: Int,
    private val repository: GameRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(GameState())

    private var _quests = repository
        .getQuestsByAdventure(adventureId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _hints = _state
        .combine(_quests) { state, quests ->
            state.copy(
                quests = quests
            )
        }
        .flatMapLatest { it ->
            if(_quests.value.isEmpty() || it.currentQuest > _quests.value.size) {
                return@flatMapLatest flow { emptyList<HintEntity>() }
            }
            repository.getHintsByQuest(_quests.value[it.currentQuest].id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _quests, _hints) { state: GameState, quests, hints ->
        state.copy(
            quests = quests,
            hints = hints
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState())

    fun nextQuest() {
        if (_state.value.currentQuest < state.value.quests.size) {
            _state.update { it -> it.copy(currentQuest = it.currentQuest + 1, currentHint = 0) }
        }
    }

    fun requestNextHint() {
        if (_state.value.currentHint < state.value.hints.size) {
            _state.update { it ->
                it.copy(currentHint = _state.value.currentHint + 1)
            }
        }
    }
}

class GameViewModelFactory(private val repository: GameRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(1, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
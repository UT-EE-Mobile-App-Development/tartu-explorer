package ee.ut.cs.tartu_explorer.feature.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.dao.QuestDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class QuestViewModel(
    private val missionId: Int,
    private val dao: QuestDao
): ViewModel() {
    private val _state = MutableStateFlow(QuestState())
    @OptIn(ExperimentalCoroutinesApi::class)
    private var _quests = _state.flatMapLatest { it -> dao.getQuestByMission(missionId) }
    @OptIn(ExperimentalCoroutinesApi::class)
//    private var _hints = _state.flatMapLatest { it -> dao.getHints(it.quests[it.currentQuest].id) }
    private var _hints = _state.flatMapLatest { it -> dao.getHints(1) }
    val state = combine(_state, _quests, _hints) { state: QuestState, quests, hints ->
        state.copy(
            quests = quests,
            hints = hints
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestState())

    fun nextQuest() {
        if(state.value.currentQuest < state.value.quests.size) {
            state.value.copy(
                currentHint = 1,
                currentQuest = state.value.currentQuest+1
            )
        }
    }

    fun requestNextHint() {
        if(state.value.currentHint < state.value.hints.size) {
            _state.update { it.copy(
                currentHint = state.value.currentHint+1
            ) }
        }
    }
}

class QuestViewModelFactory(private val dao: QuestDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestViewModel(1, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package ee.ut.cs.tartu_explorer.feature.game

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintUsageEntity
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.location.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val adventureId: Long,
    private val repository: GameRepository,
    private val locationRepository: LocationRepository
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
            if (_quests.value.isEmpty() || it.currentQuest > _quests.value.size) {
                flow { emptyList<HintEntity>() }
            } else {
                repository.getHintsByQuest(_quests.value[it.currentQuest].id)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _quests, _hints) { state: GameState, quests, hints ->
        state.copy(
            quests = quests,
            hints = hints
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState())

    private val _locationPermissionToastEvent = MutableStateFlow<Boolean>(false)
    val locationPermissionToastEvent = _locationPermissionToastEvent.asSharedFlow()

    fun guessPosition() {
        val currentQuest = state.value.quests[state.value.currentQuest]
        val targetLocation = Location("")
        targetLocation.latitude = currentQuest.latitude
        targetLocation.longitude = currentQuest.longitude

        viewModelScope.launch(Dispatchers.IO) {
            val location = locationRepository.getLastLocation()
            if (location != null) {
                val distanceToTarget = location.distanceTo(targetLocation)
                val inRadius = distanceToTarget <= currentQuest.radius
                _state.update { it -> it.copy(guessState = GuessState(distanceToTarget, inRadius)) }
            } else {
                _locationPermissionToastEvent.emit(true)
            }
        }
    }

    fun resetDebugGuessDialogue() {
        _state.update { it -> it.copy(guessState = null) }
    }

    fun nextQuest() {
        if (_state.value.currentQuest < state.value.quests.size) {
            _state.update { it ->
                it.copy(
                    currentQuest = it.currentQuest + 1,
                    currentHint = 0,
                    guessState = null
                )
            }
        }
    }

    fun requestNextHint() {
        val nextHintIndex = _state.value.currentHint + 1
        if (nextHintIndex < state.value.hints.size) {
            val hintToTrack = state.value.hints[nextHintIndex]
            viewModelScope.launch {
                repository.trackHintUsed(HintUsageEntity(hintId = hintToTrack.id, adventureId = adventureId))
            }
            _state.update { it ->
                it.copy(currentHint = nextHintIndex)
            }
        }
    }
}

class GameViewModelFactory(
    private val adventureId: Long,
    private val repository: GameRepository,
    private val locationRepository: LocationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(adventureId, repository, locationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
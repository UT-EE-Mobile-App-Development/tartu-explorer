package ee.ut.cs.tartu_explorer.feature.game

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintUsageEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestAttemptEntity
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureSessionRepository
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.location.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(
    private val adventureId: Long,
    private val repository: GameRepository,
    private val locationRepository: LocationRepository,
    private val playerRepository: PlayerRepository,
    private val adventureSessionRepository: AdventureSessionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(GameState())
    private val _sessionId = MutableStateFlow<Long?>(null)

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
            if (_quests.value.isEmpty() || it.currentQuest >= _quests.value.size) {
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

    init {
        viewModelScope.launch {
            val player = playerRepository.getPlayer().firstOrNull() ?: return@launch
            val activeSession = adventureSessionRepository.getActiveSession(adventureId, player.id)
            if (activeSession != null) {
                _sessionId.value = activeSession.id
                _state.update {
                    it.copy(
                        currentQuest = activeSession.currentQuestIndex,
                        currentHint = activeSession.currentHintIndex
                    )
                }
            } else {
                val newSessionId = adventureSessionRepository.startNewSession(adventureId, player.id)
                _sessionId.value = newSessionId
            }
        }
    }

    fun guessPosition() {
        viewModelScope.launch(Dispatchers.IO) {
            val sessionId = _sessionId.value ?: return@launch
            val currentQuestIndex = state.value.currentQuest
            val currentQuestEntity = state.value.quests.getOrNull(currentQuestIndex) ?: return@launch

            val location = locationRepository.getLastLocation()
            if (location == null) {
                _locationPermissionToastEvent.emit(true)
                return@launch
            }

            val targetLocation = Location("").apply {
                latitude = currentQuestEntity.latitude
                longitude = currentQuestEntity.longitude
            }

            val distanceToTarget = location.distanceTo(targetLocation)
            val inRadius = distanceToTarget <= currentQuestEntity.radius

            val attempt = QuestAttemptEntity(
                sessionId = sessionId,
                questId = currentQuestEntity.id,
                wasCorrect = inRadius
            )
            repository.trackQuestAttempt(attempt)

            _state.update { it.copy(guessState = GuessState(distanceToTarget, inRadius)) }
        }
    }

    fun resetDebugGuessDialogue() {
        _state.update { it -> it.copy(guessState = null) }
    }

    fun nextQuest() {
        if (state.value.currentQuest < state.value.quests.size - 1) {
            val newQuestIndex = state.value.currentQuest + 1
            _state.update { it ->
                it.copy(
                    currentQuest = newQuestIndex,
                    currentHint = 0,
                    guessState = null
                )
            }
            viewModelScope.launch {
                _sessionId.value?.let { sessionId ->
                    adventureSessionRepository.updateProgress(sessionId, newQuestIndex, 0)
                }
            }
        } else {
            viewModelScope.launch {
                _sessionId.value?.let { adventureSessionRepository.completeSession(it) }
            }
            _state.update { it.copy(guessState = null) }
        }
    }

    fun requestNextHint() {
        val nextHintIndex = _state.value.currentHint + 1
        if (nextHintIndex < state.value.hints.size) {
            viewModelScope.launch {
                val sessionId = _sessionId.value ?: return@launch
                val hintToTrack = state.value.hints[nextHintIndex]

                val usage = HintUsageEntity(
                    sessionId = sessionId,
                    hintId = hintToTrack.id
                )
                repository.trackHintUsed(usage)
                adventureSessionRepository.updateProgress(sessionId, state.value.currentQuest, nextHintIndex)
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
    private val locationRepository: LocationRepository,
    private val playerRepository: PlayerRepository,
    private val adventureSessionRepository: AdventureSessionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(adventureId, repository, locationRepository, playerRepository, adventureSessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

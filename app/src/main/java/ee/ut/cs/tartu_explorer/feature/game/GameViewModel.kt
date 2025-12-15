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
import ee.ut.cs.tartu_explorer.core.util.LevelingUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel managing the game state and logic for an adventure.
 *
 * @param adventureId ID of the adventure being played
 * @param repository Repository for game data operations
 * @param locationRepository Repository for accessing location data
 * @param playerRepository Repository for player data operations
 * @param adventureSessionRepository Repository for adventure session management
 */
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
        .flatMapLatest {
            if (_quests.value.isEmpty() || it.currentQuest >= _quests.value.size) {
                flowOf(emptyList<HintEntity>()) // Use flowOf for emitting an empty list
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
            val player = playerRepository.getActivePlayer() ?: return@launch
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

    /**
     * Attempts to guess the player's position relative to the current quest location.
     * Updates the game state with the result of the guess.
     */
    fun guessPosition() {
        viewModelScope.launch(Dispatchers.IO) {
            playerRepository.getActivePlayer()?.id ?: return@launch
            _sessionId.value ?: return@launch
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

            // Update the UI with the guess result (don't track attempt yet)
            _state.update { it.copy(guessState = GuessState(distanceToTarget, inRadius)) }
        }
    }

    /**
     * Resets the debug guess dialogue state.
     */
    fun resetDebugGuessDialogue() {
        _state.update { it.copy(guessState = null) }
    }

    /**
     * Forces the completion of the current quest, marking it as correct and awarding experience points.
     *
     * @param andMoveToNext If true, moves to the next quest after completion.
     */
    fun forceQuestCompletion(andMoveToNext: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            val playerId = playerRepository.getActivePlayer()?.id ?: return@launch
            val sessionId = _sessionId.value ?: return@launch
            val currentQuestIndex = state.value.currentQuest
            val currentQuestEntity = state.value.quests.getOrNull(currentQuestIndex) ?: return@launch

            // Debug: Mark quest as correct regardless of actual distance
            val attempt = QuestAttemptEntity(
                sessionId = sessionId,
                questId = currentQuestEntity.id,
                wasCorrect = true
            )
            repository.trackQuestAttempt(attempt)

            // Award experience points
            val hintsUsed = state.value.currentHint
            val epGained = LevelingUtil.calculateEpForQuest(hintsUsed)
            playerRepository.addExperiencePoints(playerId, epGained)

            // Move to next quest if requested
            if (andMoveToNext) {
                nextQuest()
            }
        }
    }

    /**
     * Completes the current quest based on the actual guess state.
     * Awards experience points only if the guess was within the acceptable range.
     *
     * @param andMoveToNext If true, moves to the next quest after successful completion.
     */
    fun completeQuestNormally(andMoveToNext: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            val playerId = playerRepository.getActivePlayer()?.id ?: return@launch
            val sessionId = _sessionId.value ?: return@launch
            val currentQuestIndex = state.value.currentQuest
            val currentQuestEntity = state.value.quests.getOrNull(currentQuestIndex) ?: return@launch
            val currentGuessState = state.value.guessState ?: return@launch

            // Track the attempt with actual inRadius result
            val attempt = QuestAttemptEntity(
                sessionId = sessionId,
                questId = currentQuestEntity.id,
                wasCorrect = currentGuessState.inRange
            )
            repository.trackQuestAttempt(attempt)

            // Award experience points only if in radius
            if (currentGuessState.inRange) {
                val hintsUsed = state.value.currentHint
                val epGained = LevelingUtil.calculateEpForQuest(hintsUsed)
                playerRepository.addExperiencePoints(playerId, epGained)

                // Move to next quest if in range and requested
                if (andMoveToNext) {
                    nextQuest()
                }
            }
        }
    }

    /**
     * Advances the game to the next quest, resetting relevant state and updating the session progress.
     */
    fun nextQuest() {
        if (state.value.currentQuest < state.value.quests.size - 1) {
            val newQuestIndex = state.value.currentQuest + 1
            _state.update {
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

    /**
     * Requests the next hint for the current quest, tracking its usage and updating the game state.
     */
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
            _state.update {
                it.copy(currentHint = nextHintIndex)
            }
        }
    }
}

/**
 * Factory for creating [GameViewModel] instances with required dependencies.
 *
 * @param adventureId ID of the adventure being played
 * @param repository Repository for game data operations
 * @param locationRepository Repository for accessing location data
 * @param playerRepository Repository for player data operations
 * @param adventureSessionRepository Repository for adventure session management
 * @returns A new instance of [GameViewModel]
 *
 * @throws IllegalArgumentException if the ViewModel class is not assignable
 */
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

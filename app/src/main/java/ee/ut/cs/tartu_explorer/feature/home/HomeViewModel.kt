package ee.ut.cs.tartu_explorer.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import coil3.PlatformContext
import coil3.imageLoader
import coil3.request.ImageRequest
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.util.LevelInfo
import ee.ut.cs.tartu_explorer.core.util.LevelingUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

/**
 * UI state for the Home screen.
 */
data class HomeUiState(
    val showNamePrompt: Boolean = false,
    val playerNameInput: String = "",
    val player: PlayerEntity? = null,
    val levelInfo: LevelInfo? = null,
    val adventureStatuses: Map<Long, SessionStatus> = emptyMap(),
    val showProfileSwitcher: Boolean = false,
    val players: List<PlayerEntity> = emptyList(),
    val newPlayerName: String = "",
    val readyToPlay: Boolean = false
)

/**
 * ViewModel for the Home screen.
 *
 * @param playerRepository Repository for player data
 * @param gameRepository Repository for game data
 */
class HomeViewModel(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // This coroutine handles REACTIVE updates to player/level info and then triggers status updates
        viewModelScope.launch {
            playerRepository.getActivePlayerAsFlow().collectLatest { player ->
                if (player != null) {
                    val levelInfo = LevelingUtil.calculateLevelInfo(player.experiencePoints)
                    _uiState.update {
                        it.copy(player = player, levelInfo = levelInfo, showNamePrompt = false)
                    }
                    // Now that we have a player, start observing their adventure statuses
                    gameRepository.getAdventureStatusDetails(player.id).collect { detailsMap ->
                        _uiState.update {
                            it.copy(adventureStatuses = detailsMap.mapValues { entry -> entry.value.status })
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            player = null,
                            levelInfo = null,
                            adventureStatuses = emptyMap()
                        )
                    }
                }
            }
        }

        // This coroutine handles the ONE-TIME check for the name prompt at startup
        viewModelScope.launch {
            if (playerRepository.getActivePlayer() == null) {
                _uiState.update { it.copy(showNamePrompt = true) }
            }
        }

        viewModelScope.launch {
            playerRepository.getAllPlayers().collectLatest { players ->
                _uiState.update { it.copy(players = players) }
            }
        }
    }

    /**
     * Handles changes to the player name input field.
     */
    fun onPlayerNameChange(newName: String) {
        _uiState.update { it.copy(playerNameInput = newName) }
    }

    /**
     * Saves the new player to the repository.
     */
    fun savePlayer() {
        if (uiState.value.playerNameInput.isBlank()) return

        viewModelScope.launch {
            val playerToInsert = PlayerEntity(name = uiState.value.playerNameInput)
            playerRepository.insertPlayer(playerToInsert)
        }
    }

    /**
     * Shows the profile switcher dialog.
     */
    fun showProfileSwitcher() {
        _uiState.update { it.copy(showProfileSwitcher = true) }
    }

    /**
     * Dismisses the profile switcher dialog.
     */
    fun dismissProfileSwitcher() {
        _uiState.update { it.copy(showProfileSwitcher = false) }
    }

    /**
     * Handles changes to the new player name input field.
     *
     * @param newName The new name input by the user
     */
    fun onNewPlayerNameChange(newName: String) {
        _uiState.update { it.copy(newPlayerName = newName) }
    }

    /**
     * Switches the active player.
     *
     * @param playerId The ID of the player to switch to
     */
    fun switchPlayer(playerId: Long) {
        viewModelScope.launch {
            playerRepository.switchActivePlayer(playerId)
            dismissProfileSwitcher()
        }
    }

    /**
     * Creates a new player with previously typed name.
     */
    fun createNewPlayer() {
        if (uiState.value.newPlayerName.isBlank()) return

        viewModelScope.launch {
            val playerToInsert = PlayerEntity(name = uiState.value.newPlayerName, isActive = false)
            playerRepository.insertPlayer(playerToInsert)
            _uiState.update { it.copy(newPlayerName = "") }
        }
    }

    /**
     * Prefetches the first hint images for all quests in the specified adventure.
     *
     * @param adventure The ID of the adventure
     * @param context platform context
     */
    suspend fun prefetchQuestImages(adventure: Long, context: PlatformContext) {
        val quests = gameRepository.getQuestsByAdventure(adventure).first()

        // Phase 1: preload first hint image of each quest (for preview)
        quests.forEach { quest ->
            val firstHint = gameRepository.getHintsByQuest(quest.id)
                .first()
                .firstOrNull { it.imageUrl != null }

            firstHint?.imageUrl?.let { url ->
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .build()
                context.imageLoader.enqueue(request) // fire-and-forget
            }
        }

        // Enable Play button immediately
        _uiState.update { it.copy(readyToPlay = quests.isNotEmpty()) }
    }
}

/**
 * Factory producing [HomeViewModel] instances.
 *
 * @param playerRepository Repository for player data
 * @param gameRepository Repository for game data
 * @return A new instance of [HomeViewModel]
 *
 * @throws IllegalArgumentException if the ViewModel class is not assignable
 */
class HomeViewModelFactory(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(playerRepository, gameRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

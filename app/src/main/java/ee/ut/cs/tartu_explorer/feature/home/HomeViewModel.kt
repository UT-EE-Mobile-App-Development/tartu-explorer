package ee.ut.cs.tartu_explorer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.util.LevelInfo
import ee.ut.cs.tartu_explorer.core.util.LevelingUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

data class HomeUiState(
    val showNamePrompt: Boolean = false,
    val playerNameInput: String = "",
    val player: PlayerEntity? = null,
    val levelInfo: LevelInfo? = null,
    val adventureStatuses: Map<Long, SessionStatus> = emptyMap(),
    val showProfileSwitcher: Boolean = false,
    val players: List<PlayerEntity> = emptyList(),
    val newPlayerName: String = ""
)

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
                    _uiState.update { it.copy(player = null, levelInfo = null, adventureStatuses = emptyMap()) }
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

    fun onPlayerNameChange(newName: String) {
        _uiState.update { it.copy(playerNameInput = newName) }
    }

    fun savePlayer() {
        if (uiState.value.playerNameInput.isBlank()) return

        viewModelScope.launch {
            val playerToInsert = PlayerEntity(name = uiState.value.playerNameInput)
            playerRepository.insertPlayer(playerToInsert)
        }
    }

    fun showProfileSwitcher() {
        _uiState.update { it.copy(showProfileSwitcher = true) }
    }

    fun dismissProfileSwitcher() {
        _uiState.update { it.copy(showProfileSwitcher = false) }
    }

    fun onNewPlayerNameChange(newName: String) {
        _uiState.update { it.copy(newPlayerName = newName) }
    }

    fun switchPlayer(playerId: Long) {
        viewModelScope.launch {
            playerRepository.switchActivePlayer(playerId)
            dismissProfileSwitcher()
        }
    }

    fun createNewPlayer() {
        if (uiState.value.newPlayerName.isBlank()) return

        viewModelScope.launch {
            val playerToInsert = PlayerEntity(name = uiState.value.newPlayerName, isActive = false)
            playerRepository.insertPlayer(playerToInsert)
            _uiState.update { it.copy(newPlayerName = "") }
        }
    }
}

// Factory to create the ViewModel with the repository
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

package ee.ut.cs.tartu_explorer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.util.LevelInfo
import ee.ut.cs.tartu_explorer.core.util.LevelingUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val showNamePrompt: Boolean = false,
    val playerNameInput: String = "",
    val player: PlayerEntity? = null,
    val levelInfo: LevelInfo? = null
)

class HomeViewModel(private val playerRepository: PlayerRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // This coroutine handles REACTIVE updates to player/level info
        viewModelScope.launch {
            playerRepository.getPlayerAsFlow().collect { player ->
                _uiState.update { currentState ->
                    player?.let {
                        val levelInfo = LevelingUtil.calculateLevelInfo(it.experiencePoints)
                        currentState.copy(player = it, levelInfo = levelInfo, showNamePrompt = false)
                    } ?: currentState.copy(player = null, levelInfo = null)
                }
            }
        }

        // This coroutine handles the ONE-TIME check for the name prompt at startup
        viewModelScope.launch {
            if (playerRepository.getFirstPlayer() == null) {
                _uiState.update { it.copy(showNamePrompt = true) }
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
}

// Factory to create the ViewModel with the repository
class HomeViewModelFactory(private val playerRepository: PlayerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(playerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

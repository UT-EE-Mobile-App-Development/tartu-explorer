package ee.ut.cs.tartu_explorer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. Definiert den Zustand der UI
data class HomeUiState(
    val showNamePrompt: Boolean = false,
    val playerNameInput: String = "",
    val player: PlayerEntity? = null
)

class HomeViewModel(private val playerRepository: PlayerRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Beim Start des ViewModels prüfen, ob ein Spieler existiert
        viewModelScope.launch {
            val player = playerRepository.getPlayer().firstOrNull()
            if (player == null) {
                _uiState.update { it.copy(showNamePrompt = true) } // Keinen Spieler gefunden -> Popup zeigen
            } else {
                _uiState.update { it.copy(player = player) } // Spieler gefunden
            }
        }
    }

    // 2. Event, wenn der Text im Eingabefeld sich ändert
    fun onPlayerNameChange(newName: String) {
        _uiState.update { it.copy(playerNameInput = newName) }
    }

    // 3. Event, um den neuen Spieler zu speichern
    fun savePlayer() {
        if (_uiState.value.playerNameInput.isBlank()) return // Nichts tun, wenn der Name leer ist

        viewModelScope.launch {
            val newPlayer = PlayerEntity(name = _uiState.value.playerNameInput)
            playerRepository.insertPlayer(newPlayer)

            // Popup schließen und Zustand aktualisieren
            _uiState.update { it.copy(showNamePrompt = false, player = newPlayer) }
        }
    }
}

// Factory, um das ViewModel mit dem Repository zu erstellen
class HomeViewModelFactory(private val playerRepository: PlayerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(playerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

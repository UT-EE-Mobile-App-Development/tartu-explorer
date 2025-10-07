package ee.ut.cs.tartu_explorer.feature.statistics


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ee.ut.cs.tartu_explorer.core.data.repository.StatisticsRepository
import ee.ut.cs.tartu_explorer.core.data.repository.StatsOverview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class StatsUiState {
    data object Loading : StatsUiState()
    data class Loaded(val data: StatsOverview) : StatsUiState()
    data class Error(val message: String) : StatsUiState()
}

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StatisticsRepository.from(application)

    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState

    // DEBUG Only one Player at the moment
    private val currentPlayerId: Long = 1L

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = StatsUiState.Loading
        viewModelScope.launch {
            try {
                val stats = repository.loadOverview(currentPlayerId)
                _uiState.value = StatsUiState.Loaded(stats)
            } catch (t: Throwable) {
                _uiState.value = StatsUiState.Error(t.message ?: "Unknown Error")
            }
        }
    }
}
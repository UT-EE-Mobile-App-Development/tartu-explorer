package ee.ut.cs.tartu_explorer.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
    object Idle : WeatherState()
}

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _state = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val state: StateFlow<WeatherState> = _state

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _state.value = WeatherState.Loading
            try {
                val weather = repository.getWeather(lat, lon) // no API key needed
                _state.value = WeatherState.Success(weather)
            } catch (e: Exception) {
                _state.value = WeatherState.Error("Could not load weather: ${e.message}")
            }
        }
    }
}
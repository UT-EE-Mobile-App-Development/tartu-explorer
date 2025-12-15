package ee.ut.cs.tartu_explorer.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Represents the UI state for weather data.
 */
sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
    object Idle : WeatherState()
}

/**
 * ViewModel for managing weather-related data and state.
 */
class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _state = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val state: StateFlow<WeatherState> = _state

    /**
     * Fetches weather data for the given latitude and longitude.
     *
     * @param lat Latitude of the location
     * @param lon Longitude of the location
     */
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
package ee.ut.cs.tartu_explorer.feature.weather

/**
 * Data class representing the response from the weather API.
 *
 * @property latitude The latitude of the location
 * @property longitude The longitude of the location
 * @property current_weather The current weather data
 */
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current_weather: CurrentWeather
)

/**
 * Data class representing the current weather information.
 *
 * @property temperature The current temperature in Celsius
 * @property windspeed The current wind speed in km/h
 * @property winddirection The current wind direction in degrees
 * @property weathercode The weather condition code
 * @property is_day Indicator if it is day (1) or night (0)
 * @property time The time of the weather observation
 */
data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Double,
    val weathercode: Int,
    val is_day: Int,
    val time: String
)
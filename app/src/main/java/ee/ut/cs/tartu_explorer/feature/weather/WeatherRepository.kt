package ee.ut.cs.tartu_explorer.feature.weather

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Repository for fetching weather data from the Weather API.
 */
class WeatherRepository {
    private val api: WeatherApiService = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")  // changed from OpenWeatherMap
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(WeatherApiService::class.java)

    /**
     * Fetches the current weather for the given latitude and longitude.
     *
     * @param lat Latitude of the location
     * @param lon Longitude of the location
     * @return WeatherResponse containing the current weather data
     */
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return api.getCurrentWeather(lat, lon)
    }
}
package ee.ut.cs.tartu_explorer.feature.weather

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for fetching weather data from a weather API.
 */
interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current_weather") current: Boolean = true
    ): WeatherResponse
}
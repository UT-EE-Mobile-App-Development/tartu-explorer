package ee.ut.cs.tartu_explorer.feature.weather

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WeatherRepository {
    private val api: WeatherApiService = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")  // changed from OpenWeatherMap
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(WeatherApiService::class.java)

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return api.getCurrentWeather(lat, lon)
    }
}
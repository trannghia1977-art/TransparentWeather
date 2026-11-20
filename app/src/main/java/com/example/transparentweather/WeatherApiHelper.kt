package com.example.transparentweather

import android.content.Context
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.Locale

data class WeatherInfo(
    val wardName: String,
    val temperature: Double,
    val windSpeed: Double,
    val weatherCode: Int
)

object WeatherApiHelper {

    suspend fun fetchWeather(context: Context, lat: Double, lon: Double): WeatherInfo = withContext(Dispatchers.IO) {
        val ward = getWardName(context, lat, lon)
        var temp = 27.0
        var wind = 12.0
        var code = 95

        try {
            val apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current=temperature_2m,weather_code,wind_speed_10m"
            val response = URL(apiUrl).readText()
            val json = JSONObject(response)
            val current = json.getJSONObject("current")
            temp = current.getDouble("temperature_2m")
            wind = current.getDouble("wind_speed_10m")
            code = current.getInt("weather_code")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        WeatherInfo(ward, temp, wind, code)
    }

    private fun getWardName(context: Context, lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale("vi", "VN"))
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                addr.subLocality ?: addr.locality ?: addr.subAdminArea ?: "Việt Nam"
            } else {
                "Không rõ vị trí"
            }
        } catch (e: Exception) {
            "Vị trí hiện tại"
        }
    }

    fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Trời quang đãng"
            1, 2, 3 -> "Có mây"
            45, 48 -> "Sương mù"
            51, 53, 55, 61, 63, 65 -> "Trời mưa"
            80, 81, 82 -> "Mưa rào"
            95, 96, 99 -> "Mưa dông & Sấm chớp"
            else -> "Thời tiết biến đổi"
        }
    }
}

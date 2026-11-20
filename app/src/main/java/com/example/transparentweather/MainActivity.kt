package com.example.transparentweather

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var effectView: WeatherEffectView
    private lateinit var tvLocation: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvWind: TextView

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            loadCurrentWeather()
            refreshWidgets()
        } else {
            Toast.makeText(this, "Cần quyền vị trí để hiển thị tên Xã/Phường", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        effectView = findViewById(R.id.weather_effect_view)
        tvLocation = findViewById(R.id.tv_detail_location)
        tvTemp = findViewById(R.id.tv_detail_temp)
        tvDesc = findViewById(R.id.tv_detail_desc)
        tvWind = findViewById(R.id.tv_detail_wind)

        findViewById<Button>(R.id.btn_trigger_storm).setOnClickListener {
            effectView.triggerLightning()
        }

        if (intent.getBooleanExtra("TRIGGER_EFFECT", false)) {
            effectView.post { effectView.triggerLightning() }
        }

        checkPermissionAndLoad()
    }

    private fun checkPermissionAndLoad() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            loadCurrentWeather()
            refreshWidgets()
        }
    }

    private fun loadCurrentWeather() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lifecycleScope.launch {
                        val info = WeatherApiHelper.fetchWeather(this@MainActivity, location.latitude, location.longitude)
                        tvLocation.text = info.wardName
                        tvTemp.text = "${info.temperature.roundToInt()}°C"
                        tvDesc.text = WeatherApiHelper.getWeatherDescription(info.weatherCode)
                        tvWind.text = "Sức gió: ${info.windSpeed} km/h"
                        effectView.windTilt = (info.windSpeed.toFloat() * 0.8f).coerceIn(5f, 35f)
                    }
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun refreshWidgets() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this, WeatherWidgetProvider::class.java))
        for (id in ids) {
            WeatherWidgetProvider.updateAppWidget(this, appWidgetManager, id)
        }
    }
}

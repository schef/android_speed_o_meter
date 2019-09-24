package com.example.speed_o_meter

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var maxSpeed: Float = 0.0f
    private var locationManager: LocationManager? = null
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            tvLat.setText("Lat: " + location.latitude)
            tvLon.setText("Lon: " + location.longitude)
            tvHaac.setText("Haac: " + location.accuracy)
            tvSpeed.setText((location.speed * 3.6).toInt().toString())
            if (location.accuracy < 15.0f && location.speed > maxSpeed) {
                maxSpeed = location.speed
            }
            tvMaxSpeed.setText("Max speed: " + (maxSpeed * 3.6).toInt().toString())
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener);
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }

//        fab.setOnClickListener { view ->
//            try {
//                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener);
//            } catch (ex: SecurityException) {
//                Log.d("myTag", "Security Exception, no location available")
//            }
//        }

    }
}

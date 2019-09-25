package com.example.speed_o_meter

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

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

        startApp(true)

//        fab.setOnClickListener { view ->
//            try {
//                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener);
//            } catch (ex: SecurityException) {
//                Log.d("myTag", "Security Exception, no location available")
//            }
//        }
    }


    private fun startApp(isInitial: Boolean = false) {
        if (!checkPermissions()) {
            if (isInitial) {
                requestPermission()
            }
            return
        }
        else {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
            try {
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    locationListener
                );
            } catch (ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available")
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private var permissionRequestGranted = false
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequestGranted =
            requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED
        startApp(true)
    }

    private fun checkPermissions(): Boolean {
        return if (permissionRequestGranted) true
        else ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

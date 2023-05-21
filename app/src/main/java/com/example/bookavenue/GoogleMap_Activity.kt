package com.example.bookavenue

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.viewmodel.CreationExtras


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.bookavenue.databinding.ActivityGoogleMapBinding

class GoogleMap_Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    override fun onMapReady(googleMap: GoogleMap) {
//        val geocoder = Geocoder(this)
//
//        try {
//            val address = "Sabzazar B block, House no 7, Multan road, Lahore"
//            val locations = geocoder.getFromLocationName(address, 1)
//            if (locations != null) {
//                if (locations.isNotEmpty()) {
//                    val latLng = locations[0]?.let { LatLng(it.latitude, locations[0].longitude) }
//                    latLng?.let { MarkerOptions().position(it).title("Googleplex") }
//                        ?.let { googleMap.addMarker(it) }
//                    latLng?.let { CameraUpdateFactory.newLatLngZoom(it, 14f) }
//                        ?.let { googleMap.moveCamera(it) }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }


    override fun onMapReady(googleMap: GoogleMap) {
        val intent=intent
        val geoCoder = Geocoder(this)

        try {
            val address = intent.getStringExtra("address").toString()
            val geoResult = geoCoder.getFromLocationName(address, 1)
            if (geoResult != null && geoResult.isNotEmpty()) {
                val lat = geoResult[0].latitude
                val lng = geoResult[0].longitude
                val latLng = LatLng(lat, lng)
                val markerOptions = MarkerOptions().position(latLng).title("Googleplex")
                googleMap.addMarker(markerOptions)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14f)
                googleMap.moveCamera(cameraUpdate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.map_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        // Change the map type based on the user's selection.
//        R.id.normal_map -> {
//            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//
//            true
//        }
//        R.id.hybrid_map -> {
//            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
//            true
//        }
//        R.id.satellite_map -> {
//            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
//            true
//        }
//        R.id.terrain_map -> {
//            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
//            true
//        }
//        else -> super.onOptionsItemSelected(item)
//    }
}


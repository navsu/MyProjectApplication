package com.memandis.project.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.memandis.appmain.R
import com.memandis.appmain.databinding.ActivityLocationBinding
import com.memandis.project.entry.vm.EntryEditorViewModel
import com.memandis.remote.utils.map.CODE_PERMISSION_FINE_LOCATION
import com.memandis.remote.utils.map.isGrantedFineLocationPermission

/**
 * An activity that allows user to pick a location from a Google-powered world map
 *
 * @property LOG_TAG String String Tag string for showing Debugging Log
 * @property viewModel LocationSelectorViewModel ViewModel that holds data for the location selection process
 */
class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOG_TAG = this::class.java.simpleName
    private lateinit var binding: ActivityLocationBinding
    private lateinit var viewModel: LocationSelectorViewModel

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LocationSelectorViewModel::class.java)

        intent.extras?.let {
            LocationActivityArgs.fromBundle(it).apply {
                Log.d(LOG_TAG,
                      "Received Location Bundle: ${coordinateLat.toDouble()}, ${coordinateLong.toDouble()}")
                viewModel.coordinate.value = Pair(coordinateLat.toDouble(),
                                                  coordinateLong.toDouble())
            }
        }

        binding.btmAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        // After the confirm button is pressed.
        binding.locationConfirmBtn.setOnClickListener {
            // Show a dialog asking the user to confirm
            MaterialDialog(this).show {
                cornerRadius(res = R.dimen.dialog_corner_radius)
                title(res = R.string.dialog_confirm_selected_location)

                // After the confirming, put the coordinate in the bundle and send back to the caller
                positiveButton(android.R.string.ok) {
                    val intent = Intent().apply {
                        putExtra("lat", viewModel.coordinate.value!!.first)
                        putExtra("long", viewModel.coordinate.value!!.second)
                    }

                    Log.d(LOG_TAG,
                          "Sending back Location Bundle:" +
                                  " ${viewModel.coordinate.value!!.first}," +
                                  " ${viewModel.coordinate.value!!.second}")

                    // Handling different Activity circumstance
                    if(parent == null) {
                        setResult(Activity.RESULT_OK, intent)
                    } else {
                        parent.setResult(Activity.RESULT_OK, intent)
                    }
                    finish()
                }

                negativeButton(android.R.string.cancel)
            }
        }

        prepareLocationServiceAndMap()
    }

    override fun onBackPressed() {
        // If the back button is pressed, show a dialog asking to confirm whether the user wants to exit or not.
        MaterialDialog(this).show {
            cornerRadius(res = R.dimen.dialog_corner_radius)
            title(res = R.string.dialog_exit_location_selector)

            // After the okay button is pressed.
            positiveButton(android.R.string.ok) {
                // Tell the invoker that the user has canceled.
                setResult(Activity.RESULT_CANCELED)
                finish()
            }

            negativeButton(android.R.string.cancel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun prepareLocationServiceAndMap() {
        requestServiceAndCurrentLocation()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun requestServiceAndCurrentLocation() {
        val locationManager: LocationManager = this@LocationActivity.getSystemService(
                Context.LOCATION_SERVICE) as LocationManager

        // Here, thisActivity is the current activity
        if(!this@LocationActivity.isGrantedFineLocationPermission()) {

            // Permission is not granted, Should we show an explanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@LocationActivity,
                                                                   Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Snackbar.make(findViewById(R.id.rootCoordinator),
                              getString(R.string.permission_request_location),
                              Snackbar.LENGTH_INDEFINITE
                ).show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this@LocationActivity,
                                                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                  CODE_PERMISSION_FINE_LOCATION)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if(location == null || viewModel.coordinate.value != EntryEditorViewModel.EMPTY_LOCATION) {
                    return
                }
                viewModel.coordinate.value = Pair(location.latitude, location.longitude)
                locationManager.removeUpdates(this)
            }
//            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//                // TODO("Not yet implemented")
//            }
//
//            override fun onProviderEnabled(provider: String?) {
//                // TODO("Not yet implemented")
//            }
//
//            override fun onProviderDisabled(provider: String?) {
//                Toast.makeText(this@LocationActivity, 
//                    "Location is disabled!", Toast.LENGTH_SHORT).show()
//            }
        }

        // Permission has already been granted
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                     0L,
                                   0f,
                                               locationListener)
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(g: GoogleMap) {

        g.apply {
            setMinZoomPreference(1f)
            setMaxZoomPreference(100f)
            uiSettings.setAllGesturesEnabled(true)
            uiSettings.isCompassEnabled = false

            isMyLocationEnabled = true

            setOnMapClickListener {
                setLocation(g, it)
                viewModel.coordinate.value = Pair(it.latitude, it.longitude)
            }

            setOnMyLocationClickListener {
                setLocation(g, LatLng(it.latitude, it.longitude))
                viewModel.coordinate.value = Pair(it.latitude, it.longitude)
            }
        }

        viewModel.coordinate
                .observe(this,
                         Observer {
                             Log.d(LOG_TAG, "Updating the location to (${it.first}, ${it.second})")
                             val position = LatLng(it.first, it.second)

                             g.clear()
                             val marker = MarkerOptions()
                                     .draggable(true)
                                     .position(position)
                                     .title("Selected Location")
                             g.addMarker(marker)

                             val camera= CameraUpdateFactory.newCameraPosition(
                                 CameraPosition.builder()
                                               .target(marker.position)
                                               .zoom(15f)
                                               .build()
                             )
                             g.animateCamera(camera)
                             // viewModel.coordinate.removeObserver(this)
                         })
    }

    /**
     * Set the location to the map
     *
     * @param mapInstance GoogleMap Google Map instance
     * @param location LatLng The target coordinate
     */
    private fun setLocation(mapInstance: GoogleMap, location: LatLng) {
        // Clear the markers
        mapInstance.clear()

        // Add a new marker at the target coordinate
        mapInstance.addMarker(MarkerOptions().draggable(false)
                                      .position(location))
    }

    /**
     * A host ViewModel that stores Location selection
     * @property coordinate MutableLiveData<Pair<Double, Double>> coordinate data
     */
    class LocationSelectorViewModel : ViewModel() {
        val coordinate: MutableLiveData<Pair<Double, Double>> = MutableLiveData(Pair(0.0, 0.0))
    }
}

package com.neotica.storyapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.neotica.storyapp.R
import com.neotica.storyapp.database.StoriesEntity
import com.neotica.storyapp.databinding.FragmentMapsBinding
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.ui.response.Story
import com.neotica.storyapp.ui.viewmodel.MapsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private lateinit var arrayListStories: ArrayList<Story>
    private val viewModel: MapsViewModel by viewModel()
    private var token: String? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMapsBinding.bind(view)

/*        val extra = MapsFragmentArgs.fromBundle(arguments as Bundle).arrayListStories
        arrayListStories = ArrayList(extra.toList())*/

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val prefLogin = LoginPreferences(requireContext())
        token = prefLogin.getToken()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
        token?.let { getStoriesMap(it) }
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e("neotag", "Style Parsing Failed.")
            }
        } catch (exeception: Resources.NotFoundException) {
            Log.e("neotag", "Can't Find Style. Error: ", exeception)
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getStoriesMap(token: String) {
        viewModel.getCompletedStories("Bearer $token", 1).observe(this) { response ->
            when (response) {
                is ApiResult.Loading -> {}
                is ApiResult.Success -> {
                    val stories = response.data
                    val dataStories = stories.map {
                        StoriesEntity(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            photoUrl = it.photoUrl,
                            lat = it.lat,
                            lon = it.lon,
                            createdAt = it.createdAt
                        )
                    }
                    dataStories.let {
                        for (story in it) {
                            val lat: Double = story.lat
                            val lon: Double = story.lon

                            val latLng = LatLng(lat, lon)
                            mMap.addMarker(MarkerOptions().position(latLng).title(story.name))?.tag = story.id
                        }
                        val latLng = LatLng(it[0].lat, it[0].lon)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
                    }
                }
                is ApiResult.Error -> {}
                else -> {}
            }
        }
    }
}
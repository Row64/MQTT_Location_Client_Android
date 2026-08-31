package com.example.location_client_android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlin.getValue
import android.location.Location
import com.google.android.gms.tasks.Task

class LocationFragment : Fragment() {

    // ViewModel instance
    private val viewModel: ViewModelPrimary by activityViewModels()

    // Location services client
    // https://developer.android.com/develop/sensors-and-location/location/retrieve-current
    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    var fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


    /**
     * Part of requesting location permission:
     * https://developer.android.com/develop/sensors-and-location/location/permissions/runtime#user-choice-affects-permission-grants
     *
     * Need to call registerForActivityResult during fragment creation, not before.
     * Otherwise, an exception will be thrown.
     */
    @SuppressLint("MissingPermission")
    val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.

                    println("FINE LOCATION PERMISSION GRANTED")


                    // Get location
                    // Help with parameters from: https://stackoverflow.com/questions/71137555/getcurrentlocation-method-in-kotlin
                    fusedLocationClient.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        null)
                        .addOnCompleteListener { location : Task<Location> ->
                            val lat = location.result.latitude
                            val lon = location.result.longitude

                            println("$lat, $lon")
                        }

                    // Send location updates
//                    viewModel.

                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.

                    println("COARSE LOCATION PERMISSION GRANTED")

                    // ...

                }
                else -> {
                    // No location access granted.

                    println("PERMISSION DENIED")
                }
            }
        }


    // Request location permissions
    fun requestPermissions() {

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions:
        // https://developer.android.com/training/permissions/requesting#request-permission
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    /**
     * Enables the use of Composables in a legacy fragment.
     * This requires the XML layout to have a ComposeView block
     *
     * https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/compose-in-views#compose-in-fragments
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Initialize the fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val view = inflater.inflate(R.layout.fragment_location, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // Compose UI elements go here
                MaterialTheme {

//                    SendLocationUpdatesUI(viewModel, this.context)


                    Column(
                        modifier = Modifier
                            .padding(48.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {


                        // Send location updates button
                        Button(
                            onClick = {
                                // Request permission, if needed
                                println("Arrived at permissions request...")
                                requestPermissions()
                            },
                            enabled = viewModel.locationBtnEnabled,
                        )
                        {
                            Text("Send location updates")
                        }


                    }














                }
            }
        }
        return view
    }

}










@Composable
fun SendLocationUpdatesUI(viewModel: ViewModelPrimary, context: Context) {

    Column(
        modifier = Modifier
            .padding(48.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        // Send location updates button
        Button(
            onClick = {

                // ---------------------------------------------------------------------------------
                // LOCATION PERMISSIONS

                // https://medium.com/@mahbooberezaee68/mastering-location-services-in-jetpack-compose-a-step-by-step-tutorial-lesson-1-d60dde62a07e


                // Check if app already has the needed permission
//                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED

                println("Arrived at permissions request...")

//                val request = RequestLocation()

//                request.requestPermissions()








                // Request permissions, if needed

                // Connected to broker?

                // Send location updates


            },
            enabled = viewModel.locationBtnEnabled,
//                modifier = Modifier.size(width = 80.dp, height = 20.dp)
        )
        {
            Text("Send location updates")
        }





    }
}


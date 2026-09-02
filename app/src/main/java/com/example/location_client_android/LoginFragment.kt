package com.example.location_client_android

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.getValue


class LoginFragment : Fragment() {

    // ViewModel instance
    private val viewModel: ViewModelPrimary by activityViewModels()

    // ---------------------------------------------------------------------------------------------
    // LOCATION VARIABLES AND METHODS

    // Location services client
    // https://developer.android.com/develop/sensors-and-location/location/retrieve-current
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Device location object
    private var deviceLocation: Location? = null

    // Location request settings
    // https://developer.android.com/develop/sensors-and-location/location/change-location-settings
    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000
    )
        .setMinUpdateIntervalMillis(5000)
        .build()

    // Location callback
    // https://developer.android.com/develop/sensors-and-location/location/request-updates
    private lateinit var locationCallback: LocationCallback


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

                viewModel.updateOutputMessage("Sending location updates. Check your broker for results.")

                // Define the location update callback
                // https://developer.android.com/develop/sensors-and-location/location/request-updates
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult ?: return
                        for (location in locationResult.locations) {

                            // Do something here with location data

                            // For testing
                            println("${location.latitude}, ${location.longitude}, ${location.time}")

                            // Send data to MQTT broker
                            viewModel.sendMessage(
                                "R64_LOCATION_UPDATE",
                                "${location.latitude}, ${location.longitude}".toByteArray()
                            )


                        }
                    }
                }


                // Get last known location as a baseline
                // https://developer.android.com/develop/sensors-and-location/location/retrieve-current#kotlin
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        deviceLocation = location
                    }


                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )



            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                println("COARSE LOCATION PERMISSION GRANTED")
                viewModel.updateOutputMessage("ERROR: Only approximate location was granted. " +
                        "The app cannot operate as intended and will not permit location updates. " +
                        "Please grant this app access to exact location in the system settings.")

            }
            else -> {
                // No location access granted.
                println("PERMISSION DENIED")
                viewModel.updateOutputMessage("ERROR: Location permissions were denied. " +
                        "The app cannot operate as intended and will not permit location updates. " +
                        "Please grant this app access to exact location in the system settings.")
            }
        }
    }


    // Request location permissions
    fun requestPermissions() {

        // Before you perform the actual permission request, check whether your app
        // already has the permissions:
        // https://developer.android.com/training/permissions/requesting#request-permission
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // ---------------------------------------------------------------------------------------------


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

        val view = inflater.inflate(R.layout.layout_fragment_login, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // Compose UI elements go here
                MaterialTheme {

//                    ConnectScreen(viewModel)

                    Column(
                        modifier = Modifier
                            .padding(48.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                    // -----------------------------------------------------------------------------
                    // CONNECT

                        Title()

                        // Login components
                        var stateHost = rememberTextFieldState()
                        var statePort = rememberTextFieldState()
                        var stateUser = rememberTextFieldState()
                        var statePass = rememberTextFieldState()

                        // Host field
                        OutlinedTextField(
                            state = stateHost,
                            label = { Text("Host") },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false
                            ),
                            isError = viewModel.loginFieldError,
                            enabled = viewModel.loginFieldEnabled
                        )

                        // Port field
                        OutlinedTextField(
                            state = statePort,
                            label = { Text("Port") },
                            inputTransformation = InputTransformation.maxLength(5),
                            lineLimits = TextFieldLineLimits.SingleLine,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false,
                                keyboardType = KeyboardType.Number
                            ),
                            isError = viewModel.loginFieldError,
                            enabled = viewModel.loginFieldEnabled
                        )

                        // Username field
                        OutlinedTextField(
                            state = stateUser,
                            label = { Text("User") },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false
                            ),
                            enabled = viewModel.loginFieldEnabled
                        )

                        // Password field
                        OutlinedSecureTextField(
                            state = statePass,
                            label = { Text("Password") },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false
                            ),
                            enabled = viewModel.loginFieldEnabled
                        )

                        // Buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            // Connect button
                            Button(
                                onClick = {

                                    // Send login data to the view model
                                    viewModel.inputHost.value = stateHost.text.toString()
                                    viewModel.inputPort.value = statePort.text.toString()
                                    viewModel.inputUser.value = stateUser.text.toString()
                                    viewModel.inputPass.value = statePass.text.toString()

                                    // Attempt the connection to the MQTT broker
                                    // Also completes a basic input validation check
                                    viewModel.tryConnect()

                                },
                                enabled = viewModel.connectBtnEnabled,
                            )
                            {
                                Text(text = stringResource(R.string.connect_btn_connect))
                            }

                            // Disconnect button
                            FilledTonalButton(
                                onClick = {

                                    // Disconnect from server or cancel connection attempt
                                    viewModel.disconnectFromBroker()
                                    viewModel.updateOutputMessage("Disconnected from server.")

                                    // Reset UI state
                                    viewModel.toggleConnectBtn(true)
                                    viewModel.toggleLoginFieldEnabled(true)
                                    viewModel.toggleDisconnectBtn(false)
                                    viewModel.toggleLocationBtnEnabled(false)
                                    viewModel.toggleLocationCancelBtnEnabled(false)

                                    // Cancel location updates
                                    println("Canceled location updates")

                                    // Removing callback throws exception if not client is not yet initialized
                                    try {
                                        fusedLocationClient.removeLocationUpdates(locationCallback)
                                        viewModel.updateOutputMessage("Disconnected from broker and canceled location updates.")
                                    }
                                    // No need to do anything if exception is caught
                                    catch(e: Exception) { }
                                },
                                enabled = viewModel.disconnectBtnEnabled
                            )
                            {
                                Text(text = stringResource(R.string.disconnect_btn_cancel))
                            }
                        }

                    // -----------------------------------------------------------------------------
                    // LOCATION UI

                        // Send location updates button
                        Button(
                            onClick = {

                                viewModel.updateOutputMessage("Attempting to send location updates...")
                                viewModel.toggleLocationBtnEnabled(false)
                                viewModel.toggleLocationCancelBtnEnabled(true)

                                // Request permission, if needed
                                println("Arrived at permissions request...")
                                requestPermissions()
                            },
                            enabled = viewModel.locationBtnEnabled,
                        )
                        {
                            Text("Send location updates")
                        }

                        // Stop updates button
                        FilledTonalButton(
                            onClick = {

                                viewModel.updateOutputMessage("Location updates canceled.")
                                println("Canceled location updates")

                                viewModel.toggleLocationBtnEnabled(true)
                                viewModel.toggleLocationCancelBtnEnabled(false)

                                // Cancel the location updates
                                fusedLocationClient.removeLocationUpdates(locationCallback)
                            },
                            enabled = viewModel.locationCancelBtnEnabled,
                        )
                        {
                            Text("Stop sending updates")
                        }




                    // -----------------------------------------------------------------------------
                    // STATUS UPDATES

                        // Status
                        Text(
                            text = viewModel.outputMessage,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .height(250.dp)
                        )



                    // -----------------------------------------------------------------------------

                    }

                }
            }
        }
        return view
    }


}


@Composable
fun Title() {
    Text(
        text = stringResource(R.string.login_screen_title),
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}


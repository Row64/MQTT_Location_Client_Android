package com.example.location_client_android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.location_client_android.LoginFragment

class ViewModelPrimary : ViewModel() {

    /**
     * StateFlow with custom getters.
     * https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state
     *
     * Made private to ensure that states are not modified by external classes.
     */
//    private val _uiStateLogin = MutableStateFlow(LoginFragment())
//    val uiStateLogin: StateFlow<LoginFragment> = _uiStateLogin.asStateFlow()
//    private val _uiStateLocation = MutableStateFlow(LocationFragment())
//    val uiStateLocation: StateFlow<LocationFragment> = _uiStateLocation.asStateFlow()


    // Compose UI variables
    var connectBtnEnabled by mutableStateOf(true)
        private set // Only change the state from within this class



    // Raw user input variables
    // Values retrieved from text boxes are CharSequence by default
    var inputHost = MutableLiveData<String?>()
    var inputPort = MutableLiveData<String?>()
    var inputUser = MutableLiveData<String?>()
    var inputPass = MutableLiveData<String?>()

    // MQTT login object
    private var mqLogin = MqLogin()

    // MQTT client object
    lateinit var mqClient: MqClient


    fun tryConnect() {


        // disable Connect button
        // only re-enable button after successful try or catching an exception
        // ...

        try {
            setCredentials()

        }
        catch(e: MqLoginException) {

            println(e.message)

            // ...

            // Re-enable Connect button
        }

    }

    /**
     * Set the inputted credentials to the login object
     */
    private fun setCredentials() {

        // Check for required minimum credentials
        if (inputHost.value.equals(null)
            || inputPort.value.equals(null)
            || inputHost.value.equals("")
            || inputPort.value.equals(""))
        {
            throw MqLoginException(
                "Missing a required credential. Host and Port are required." +
                    "\n\tHost: ${inputHost.value}" +
                    "\n\tPort: ${inputPort.value}")

            // Set OutlinedTextField.isError to True
            // https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-outlined-text-field.html

            // ...

        }
        else {

            // Convert the port entry into an Int
            // ...

        }



    }



    // UI methods

    fun toggleConnectBtn(toggle: Boolean) {
        connectBtnEnabled = toggle
    }


}
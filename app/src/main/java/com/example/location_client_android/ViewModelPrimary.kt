package com.example.location_client_android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
    var loginFieldError by mutableStateOf(false)
        private set



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

        try {

            // Basic input validation and assign user input to MqLogin object
            setCredentials()

            // Disable the Connect button starting after the basic credentials check
            // No need to disable the button every time it's selected, especially if there's an error
            connectBtnEnabled = false

            // Attempt to connect ...

        }
        catch(e: MqCredentialException) {
            // This exception should only be caught if a required credential is missing,
            // or if the format is bad.
            // Host and Port are required. Port must be an integer.

            println(e.message)
            toggleFieldError(true)
            return

            // NOTIFY USER
            // ...

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
            // Make fields show error indicator
            toggleFieldError(true)

            throw MqCredentialException(
                "Missing a required credential. Host and Port are required." +
                    "\n\tHost: ${inputHost.value}" +
                    "\n\tPort: ${inputPort.value}")
        }
        else {

            // Remove error visual in case it had been enabled from a previous failed check
            toggleFieldError(false)

            // Convert the port entry into an Int
            var portInt: Int
            try {
                portInt = inputPort.value!!.toInt()

                // Ensure port is within valid range
                if (!(portInt in 1..65535)) {
                    throw MqCredentialException("Provided port number is out of range. Valid range is: 1 - 65,535")
                }

            }
            catch(e: NumberFormatException) {
                throw MqCredentialException("Port must be a positive integer within the range of 1 to 65,535.")
            }

            // Assign user inputs to MqLogin object
            // ...



        }



    }



    // ---------------------------------------------------------------------------------------------
    // UI METHODS

    fun toggleConnectBtn(toggle: Boolean) {
        connectBtnEnabled = toggle
    }


    fun toggleFieldError(toggle: Boolean) {
        loginFieldError = toggle
    }

    // ---------------------------------------------------------------------------------------------


}
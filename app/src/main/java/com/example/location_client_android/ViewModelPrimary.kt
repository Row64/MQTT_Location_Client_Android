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
    var disconnectBtnEnabled by mutableStateOf(false)
        private set
    var loginFieldError by mutableStateOf(false)
        private set
    var loginFieldEnabled by mutableStateOf(true)
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


    /**
     * This method completes a basic input validation and attempts to establish a
     * connection to a broker. If the input validation fails, a custom exception
     * (MqCredentialException) is thrown, and the method ends.
     *
     * If the input validation passes, the method attempts to establish a connection
     * to the broker.
     */
    fun tryConnect() {

        try {

            // Basic input validation and assign user input to MqLogin object
            setCredentials()

            // Disable the Connect button starting after the basic credentials check
            // No need to disable the button every time it's selected, especially if there's an error
            // Also disable the text fields during a connection
            connectBtnEnabled = false
            disconnectBtnEnabled = true
            loginFieldEnabled = false

            // Initialize client and attempt to connect
            mqClient = MqClient(mqLogin)
            mqClient.mqConnect()

        }
        catch(e: MqCredentialException) {
            // This exception should only be caught if a required credential is missing,
            // or if the format is bad.
            // Host and Port are required. Port must be an integer.

            println(e.message)
//            toggleFieldError(true)
            loginFieldError = true

            // NOTIFY USER
            // ...

            return

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
            loginFieldError = true

            throw MqCredentialException(
                "Missing a required credential. Host and Port are required." +
                    "\n\tHost: ${inputHost.value}" +
                    "\n\tPort: ${inputPort.value}")
        }
        else {

            // Remove error visual in case it had been enabled from a previous failed check
            loginFieldError = false

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

            // Assign required user inputs to MqLogin object
            mqLogin.host = inputHost.value!!
            mqLogin.port = portInt

            // Assign additional inputs to the MqLogin object, if present.
            // Username and password are only assigned if they are both present.
            if (!(inputUser.value.equals(null)
                || inputPass.value.equals(null)
                || inputUser.value.equals("")
                || inputPass.value.equals("")))
            {
                mqLogin.user = inputUser.value
                mqLogin.pass = inputPass.value
            }


            // FOR TESTING
            println("MqLogin object values:" +
                    "\n\tHost: ${mqLogin.host}" +
                    "\n\tPort: ${mqLogin.port}" +
                    "\n\tUser: ${mqLogin.user}" +
                    "\n\tPass: ${mqLogin.pass}")

        }


    }




    // FOR TESTINGS
    fun sendTestMessage() {

        try {
            mqClient.mqPublish3("TEST", "Test message from client".toByteArray())
        }
        catch(e: UninitializedPropertyAccessException) {
            // For when a message is sent before the client object is initialized.
            // The client is initialized in tryConnect()

            println("Could not send message because the client is not yet initialized. Try connecting first.")
        }
    }



    // ---------------------------------------------------------------------------------------------
    // UI METHODS

    fun toggleConnectBtn(toggle: Boolean) {
        connectBtnEnabled = toggle
    }


    fun toggleDisconnectBtn(toggle: Boolean) {
        disconnectBtnEnabled = toggle
    }


    fun toggleFieldError(toggle: Boolean) {
        loginFieldError = toggle
    }


    fun toggleLoginFieldEnabled(toggle: Boolean) {
        loginFieldEnabled = toggle
    }

    // ---------------------------------------------------------------------------------------------



}
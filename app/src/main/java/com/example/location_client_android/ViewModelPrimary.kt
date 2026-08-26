package com.example.location_client_android

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
        }



    }


}
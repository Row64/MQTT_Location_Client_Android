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
    // Values retrieved from text boxes are Strings by default
    var inputHost = MutableLiveData<String?>()
    var inputPort = MutableLiveData<String?>()
    var inputUser = MutableLiveData<String?>()
    var inputPass = MutableLiveData<String?>()



}
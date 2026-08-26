package com.example.location_client_android

class MqLogin() {

    constructor(hostInput: String, portInput: Int) : this() {
        host = hostInput
        port = portInput
    }

    constructor(hostInput: String, portInput: Int, userInput: String, passInput: String) : this(hostInput, portInput) {
        user = userInput
        pass = passInput
    }


    // Core login variables
    lateinit var host: String
    var port: Int = -1
    var user: String? = null
    var pass: String? = null

}

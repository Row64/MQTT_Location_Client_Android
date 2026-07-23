/**
 * The purpose of this class is to gather the login credentials
 * from the user.
 *
 * This class is intended to be passed into the MqClient class,
 * which accesses the credentials from the object as needed.
 *
 * This class has two constructors.
 *  - The primary constructor takes no arguments and depends on
 *      retrieving the credentials from the UI with a method.
 *  - The secondary constructor accepts a manual input for each
 *      field. This is primarily intended to be used for testing.
 *
 */

package com.example.mqtt_client_v4

class MqLogin() {

    constructor(hostInput: String, portInput: Int) : this() {
        host = hostInput
        port = portInput
    }

    constructor(hostInput: String, portInput: Int, userInput: String, passInput: String) : this(hostInput, portInput) {
        user = userInput
        pass = passInput
    }



    // Should I make these private? **************************************************

    // Core login variables
    lateinit var host: String
    var port: Int = -1
    var user: String? = null
    var pass: String? = null



}

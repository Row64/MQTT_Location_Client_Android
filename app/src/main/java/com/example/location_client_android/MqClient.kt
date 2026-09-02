/**
 * Implementation of the HiveMQ Client library is based on the documentation:
 * https://hivemq.github.io/hivemq-mqtt-client/
 * https://www.hivemq.com/blog/mqtt-client-library-enyclopedia-hivemq-mqtt-client/
 */

package com.example.location_client_android

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck
import java.util.UUID

class MqClient(val login: MqLogin) {

    // ---------------------------------------------------------------------------------------------
    // MQTT CLIENT OBJECTS

    // Blocking MQTT 3 client
    private var client3Blocking = MqttClient.builder()
        .useMqttVersion3()
        .identifier(UUID.randomUUID().toString())
        .serverHost(login.host)
        .serverPort(login.port)
        .sslWithDefaultConfig()
        .buildBlocking()

    // Blocking MQTT 5 client
    private var client5Blocking = MqttClient.builder()
        .useMqttVersion5()
        .identifier(UUID.randomUUID().toString())
        .serverHost(login.host)
        .serverPort(login.port)
        .sslWithDefaultConfig()
        .buildBlocking()

    // Tracks the determined MQTT verison
    lateinit private var mqVersion: String

    // ---------------------------------------------------------------------------------------------
    // CONNECT METHODS

    /**
     * This is the primary connection method used.
     *
     * The HiveMQ Client library does not include automatic fallback features. So,
     * if you try to use a v5 client and connect to a v3.1.1 broker, the connection
     * will fail, and the client will not automatically fall back to v3.
     *
     * To handle this, in this method, I first attempt to connect to a broker using the
     * v5 client. If this connection attempt fails, then mqConnect() will attempt the same
     * connection using the v3 client.
     *
     * A truly failed connection occurs only if the client fails to connect to the broker
     * with both version 5 and 3.1.1.
     *
     * I used the blocking connect methods in order to force the logic to flow linearly, so that
     * I could track the connection status and update the UI accordingly in the view model. To
     * prevent these blocking methods from halting the main application thread, I wrapped the call
     * in a coroutine in the view model that calls this method.
     */
    fun mqConnectBlocking(): Boolean {

        val connAck5: Mqtt5ConnAck
        val connAck3: Mqtt3ConnAck

        // No authentication login (no username and password)
        if (login.user == null) {

            println("(BLOCKING) Attempting to connect with version 5 using no authentication")

            // Blocking connect throws an exception if the connection fails
            try {
                connAck5 = client5Blocking.connect()
            }
            catch (e: Exception) {
                println("Failed to connect using v5 with no authentication")
                println("Exception caught when trying to connect:")
                println(e.message)

                println("(BLOCKING) Attempting to connect with version 3 using no authentication")

                // Try to connect with v3 if v5 fails
                try {
                    connAck3 = client3Blocking.connect()
                }
                catch (e: Exception) {

                    println("Failed to connect using v3 with no authentication")
                    println("Exception caught when trying to connect:")
                    println(e.message)

                    println("Cannot connect to client.")
                    return false
                }

                println("Successfully connected using version 3, no authentication")
                println(connAck3)
                mqVersion = "v3"
                return true

            }

            println("Successfully connected using version 5, no authentication")
            mqVersion = "v5"
            println(connAck5)
            return true
        }
        // For username and no password
        else if (!(login.user == null) && login.pass == null) {

            println("(BLOCKING) Attempting to connect with version 5 using username and no password")

            // Blocking connect throws an exception if the connection fails
            try {
                connAck5 = client5Blocking.connectWith()
                    .simpleAuth()
                    .username(login.user!!)
                    .applySimpleAuth()
                    .send()
            }
            catch (e: Exception) {
                println("Failed to connect using v5")
                println("Exception caught when trying to connect:")
                println(e.message)

                println("(BLOCKING) Attempting to connect with version 3 using username and no password")

                // Try to connect with v3 if v5 fails
                try {
                    connAck3 = client3Blocking.connectWith()
                        .simpleAuth()
                        .username(login.user!!)
                        .applySimpleAuth()
                        .send()
                }
                catch (e: Exception) {

                    println("Failed to connect using v3")
                    println("Exception caught when trying to connect:")
                    println(e.message)

                    println("Cannot connect to client.")
                    return false
                }

                println("Successfully connected using version 3")
                println(connAck3)
                mqVersion = "v3"
                return true
            }

            println("Successfully connected using version 5")
            mqVersion = "v5"
            println(connAck5)
            return true
        }
        // Basic authentication login
        else {

            println("(BLOCKING) Attempting to connect with version 5 using basic authentication")

            // Blocking connect throws an exception if the connection fails
            try {
                connAck5 = client5Blocking.connectWith()
                    .simpleAuth()
                    .username(login.user!!)
                    .password(login.pass!!.toByteArray())
                    .applySimpleAuth()
                    .send()
            }
            catch (e: Exception) {
                println("Failed to connect using v5")
                println("Exception caught when trying to connect:")
                println(e.message)

                println("(BLOCKING) Attempting to connect with version 3 using basic authentication")

                // Try to connect with v3 if v5 fails
                try {
                    connAck3 = client3Blocking.connectWith()
                        .simpleAuth()
                        .username(login.user!!)
                        .password(login.pass!!.toByteArray())
                        .applySimpleAuth()
                        .send()
                }
                catch (e: Exception) {

                    println("Failed to connect using v3")
                    println("Exception caught when trying to connect:")
                    println(e.message)

                    println("Cannot connect to client.")
                    return false
                }

                println("Successfully connected using version 3")
                println(connAck3)
                mqVersion = "v3"
                return true
            }

            println("Successfully connected using version 5")
            mqVersion = "v5"
            println(connAck5)
            return true
        }

    }


    // ---------------------------------------------------------------------------------------------
    // PUBLISH METHODS

    /**
     * This method selects which protocol to use based on the protocol version previously
     * identified in the connection (assuming a connection was attempted prior to calling).
     *
     * These publish methods are blocking, so they need to be encapsulated in a coroutine
     * in the view model, which calls them.
     */
    fun mqPublishBlocking(topic: String, payload: ByteArray) {

        // Call publish method based on determined MQTT connection version
        when (mqVersion) {
            "v5"    -> mqPublish5Blocking(topic, payload)
            "v3"    -> mqPublish3Blocking(topic, payload)
            else    -> mqPublish5Blocking(topic, payload) // The publish method will print an error when not connected
        }
    }

    // Send a message using MQTT 3.1.1
    private fun mqPublish3Blocking(topic: String, payload: ByteArray) {
        try {
            client3Blocking.publishWith()
                .topic(topic)
                .payload(payload)
                .qos(MqttQos.EXACTLY_ONCE)
                .send()

            println("Sent message with version 3")
        }
        catch (e: Exception) {
            println("Caught exception when trying to send message with version 3")
            println(e.message)
        }
    }

    // Send a message using MQTT 5
    private fun mqPublish5Blocking(topic: String, payload: ByteArray) {
        try {
            client5Blocking.publishWith()
                .topic(topic)
                .payload(payload)
                .qos(MqttQos.EXACTLY_ONCE)
                .send()

            println("Sent message with version 5")
        }
        catch (e: Exception) {
            println("Caught exception when trying to send message with version 5")
            println(e.message)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // DISCONNECT

    fun disconnectAll() {

        // Blocking clients throw an exception if a disconnect attempt is made when not connected
        try {
            client5Blocking.disconnect()
        }
        catch (e: Exception) {
            println("Caught exception when disconnecting from v5")
            println(e.message)
            println(e.cause)
        }

        try {
            client3Blocking.disconnect()
        }
        catch (e: Exception) {
            println("Caught exception when disconnecting from v3")
            println(e.message)
            println(e.cause)
        }

        println("Completed disconnect")

    }

    // ---------------------------------------------------------------------------------------------



}
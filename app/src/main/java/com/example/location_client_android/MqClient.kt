/**
 * Implementation of the HiveMQ Client library is based on the documentation:
 * https://hivemq.github.io/hivemq-mqtt-client/
 * https://www.hivemq.com/blog/mqtt-client-library-enyclopedia-hivemq-mqtt-client/
 */


/**
 * IMPLEMENTATION GOALS
 *
 *  - Need to add support for no authentication
 */


package com.example.location_client_android

import com.google.android.gms.tasks.Tasks.await
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck
import java.util.UUID
import java.util.concurrent.CompletableFuture

class MqClient(val login: MqLogin) {

    // ---------------------------------------------------------------------------------------------
    // MQTT CLIENT OBJECTS

    // MQTT 3 client
    private var client3: Mqtt3AsyncClient = MqttClient.builder()
        .useMqttVersion3()
        .identifier(UUID.randomUUID().toString())
        .serverHost(login.host)
        .serverPort(login.port)
        .sslWithDefaultConfig()
        .buildAsync()

    // MQTT 5 client
    private var client5: Mqtt5AsyncClient = MqttClient.builder()
        .useMqttVersion5()
        .identifier(UUID.randomUUID().toString())
        .serverHost(login.host)
        .serverPort(login.port)
        .sslWithDefaultConfig()
        .buildAsync()

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
     * in a coroutine in the view model.
     */
    fun mqConnectBlocking(): Boolean {

        // No authentication login (no username and password)


        // Basic authentication login

        println("(BLOCKING) Attempting to connect with version 5 using basic authentication")

        val connAck5: Mqtt5ConnAck
        val connAck3: Mqtt3ConnAck

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


//    fun mqConnect() {
//
//        println("Attempting to connect with version 5 using basic authentication")
//
//        client5.connectWith()
//            .simpleAuth()
//            .username(login.user!!)
//            .password(login.pass!!.toByteArray())
//            .applySimpleAuth()
//            .send()
//            .whenCompleteAsync { _, throwable ->
//                // For failure
//                if (throwable != null) {
//                    println("v5 connection failed")
//
//                    // Try to connect with v3 if v5 fails
//                    mqConnect3()
//                }
//                // For success
//                else {
//                    println("Successfully connected with v5")
//                    mqVersion = "v5"
//                }
//
//            }
//
//        // FOR TESTING
//        println("End of mqConnect()")
//    }
//
//
//    private fun mqConnect3() {
//
//        println("Got to mqConnect3()")
//        println("Attempting to connect with version 3 using basic authentication")
//
//        client3.connectWith()
//            .simpleAuth()
//            .username(login.user!!)
//            .password(login.pass!!.toByteArray())
//            .applySimpleAuth()
//            .send()
//            .whenCompleteAsync { _, throwable ->
//                // For failure
//                if (throwable != null) {
//                    println("v3 connection failed")
//                    throw MqFailedConnection3Exception("")
//                    mqVersion = "ERROR"
//                }
//                // For success
//                else {
//                    println("Successfully connected with v3")
//                    mqVersion = "v3"
//                }
//
//            }
//
//    }
//
//
//    private fun mqConnect5() {
//
//        println("Got to mqConnect3()")
//        println("Attempting to connect with version 5 using basic authentication...")
//
//        client5.connectWith()
//            .simpleAuth()
//            .username(login.user!!)
//            .password(login.pass!!.toByteArray())
//            .applySimpleAuth()
//            .send()
//            .whenCompleteAsync { _, throwable ->
//                // For failure
//                if (throwable != null) {
//                    println("v5 connection failed")
//                    throw MqFailedConnection5Exception("")
//                }
//                // For success
//                else {
//                    println("Successfully connected with v5")
//                }
//
//            }
//    }



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


//    fun mqPublish(topic: String, payload: ByteArray) {
//
//        // Call publish message version based on determined MQTT connection version
//        when (mqVersion) {
//            "v5"    -> mqPublish5(topic, payload)
//            "v3"    -> mqPublish3(topic, payload)
//            else    -> mqPublish5(topic, payload) // The publish method will print an error when not connected
//        }
//
//    }
//
//
//    private fun mqPublish3(topic: String, payload: ByteArray) {
//        client3.publishWith()
//            .topic(topic)
//            .payload(payload)
//            .qos(MqttQos.EXACTLY_ONCE)
//            .send()
//            .whenComplete { publish, throwable ->
//                // For error
//                if (throwable != null) {
//                    println("Failed to send message")
//                    println(throwable.message)
//                }
//                // For success
//                else {
//                    println("Message sent")
//                }
//            }
//    }
//
//
//    private fun mqPublish5(topic: String, payload: ByteArray) {
//        client5.publishWith()
//            .topic(topic)
//            .payload(payload)
//            .qos(MqttQos.EXACTLY_ONCE)
//            .send()
//            .whenComplete { publish, throwable ->
//                // For error
//                if (throwable != null) {
//                    println("Failed to send message")
//                    println(throwable.message)
//                }
//                // For success
//                else {
//                    println("Message sent")
//                }
//            }
//    }


    // ---------------------------------------------------------------------------------------------
    // RECEIVE METHODS

    // (This app does not currently need to receive messages from a broker)


    // ---------------------------------------------------------------------------------------------
    // DISCONNECT


    fun disconnectAll() {

        client3.disconnect()
        client5.disconnect()

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
    // TESTING

    /**
     * Connect to the broker using MQTT version 3.1.1
     *
     * Supports basic authentication
     */
    fun mqConnectAndSend3() {

        // Check for basic authentication
        if (login.user != null && login.pass != null) {
            // Connect with basic authentication
            client3.connectWith()
                .simpleAuth()
                .username(login.user!!)
                .password(login.pass!!.toByteArray())
                .applySimpleAuth()
                .send()
                .whenComplete({ connAck, throwable ->
                    if (throwable != null) {
                        // handle failure
                        println("TEST - Failed to connect")
                        println(throwable.message)
                    } else {
                        // setup subscribes or start publishing
                        println("Successfully connected")

                        // Send a single test message
                        client3.publishWith()
                            .topic("TEST")
                            .payload("Message from mqClient3 with basic auth".toByteArray())
                            .qos(MqttQos.EXACTLY_ONCE)
                            .send()
                            .whenCompleteAsync { publish, throwable ->
                                if (throwable != null) {
                                    println("Could not send message")
                                    println(throwable.message)
                                } else {
                                    println("Sent message")
                                }
                            }


                    }
                })

        }
        else {
            // Connect with no authentication
            client3.connect()
                .whenComplete({ connAck, throwable ->
                    if (throwable != null) {
                        // handle failure
                        println("TEST - Failed to connect")
                        println(throwable.message)
                    } else {
                        // setup subscribes or start publishing
                        println("Successfully connected")

                        // Send a single test message
                        client3.publishWith()
                            .topic("TEST")
                            .payload("Message from mqClient3 with no auth".toByteArray())
                            .qos(MqttQos.EXACTLY_ONCE)
                            .send()
                            .whenCompleteAsync { publish, throwable ->
                                if (throwable != null) {
                                    println("Could not send message")
                                    println(throwable.message)
                                } else {
                                    println("Sent message")
                                }
                            }


                    }
                })

        }

    }


    /**
     * Publish a message to an MQTT 3.1.1 broker
     */
    fun mqPublish3(topic: String, payload: ByteArray?, qos: MqttQos) {

        client3.publishWith()
            .topic(topic)
            .payload(payload)
            .qos(qos)
            .send()
            .whenComplete { mqtt3Publish, throwable ->
                if (throwable != null) {
                    // Handle failure to publish
                    println("Could not send message with topic: $topic")
                    println(throwable.message)
                }
                else {
                    // Handle successful publish
                    println("Message sent with topic: $topic")
                }
            }
    }












    /**
     * FOR TESTING ONLY! *********************************************************************************************
     * Remove these functions when finished
     */





    fun testConnect_04() {
        // Attempt the basic auth method

        // DOES NOT WORK
        // Issue with introducing setBasicAuth()
        /**
         * Is the error occurring because I'm separating the dot notation
         * blocks?
         *
         * Do all of the sub-functions in .connectWith() need to be together?
         */

        client3 = setBasicAuth(client3)

        // Connect
        client3.connectWith()
            .send()
            .whenComplete({ connAck, throwable ->
                if (throwable != null) {
                    // handle failure
                    println("TEST - Failed to connect")
                    println(throwable.message)
                } else {
                    // setup subscribes or start publishing
                    println("Successfully connected")

                    // Send a single test message
                    client3.publishWith()
                        .topic("TEST")
                        .payload("Message from testConnect_04".toByteArray())
                        .qos(MqttQos.EXACTLY_ONCE)
                        .send()
                        .whenCompleteAsync { publish, throwable ->
                            if (throwable != null) {
                                println("Could not send message")
                                println(throwable.message)
                            } else {
                                println("Sent message")
                            }
                        }
                }
            })


    }


    fun testConnect_03(){
        // Attempt use the variable login

        // THIS WORKS!

        client3.connectWith()
            .simpleAuth()
            .username(login.user!!)
            .password(login.pass!!.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete({ connAck, throwable ->
                if (throwable != null) {
                    // handle failure
                    println("TEST - Failed to connect")
                    println(throwable.message)
                } else {
                    // setup subscribes or start publishing
                    println("Successfully connected")

                    // Send a single test message
                    client3.publishWith()
                        .topic("TEST")
                        .payload("Message from testConnect_03".toByteArray())
                        .qos(MqttQos.EXACTLY_ONCE)
                        .send()
                        .whenCompleteAsync { publish, throwable ->
                            if (throwable != null) {
                                println("Could not send message")
                                println(throwable.message)
                            } else {
                                println("Sent message")
                            }
                        }
                }
            })
    }

    fun testConnect_02() {
        // Same test as before, but with class client instead of method client

        /**
         *
         * THIS WORKS
         *
         * So, the issue is not with the class' client.
         */

        client3.connectWith()
            .simpleAuth()
            .username("row64")
            .password("temp7777".toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete({ connAck, throwable ->
                if (throwable != null) {
                    // handle failure
                    println("TEST - Failed to connect")
                    println(throwable.message)
                } else {
                    // setup subscribes or start publishing
                    println("Successfully connected")

                    // Send a single test message
                    client3.publishWith()
                        .topic("TEST")
                        .payload("Message from testConnect_02".toByteArray())
                        .qos(MqttQos.EXACTLY_ONCE)
                        .send()
                        .whenCompleteAsync { publish, throwable ->
                            if (throwable != null) {
                                println("Could not send message")
                                println(throwable.message)
                            } else {
                                println("Sent message")
                            }
                        }
                }
            })
    }




    fun testConnect_01() {

        val client3TEST: Mqtt3AsyncClient = MqttClient.builder()
            .useMqttVersion3()
            .identifier("my-mqtt-client-id")
            .serverHost("5ab2c7f979c54060853470b0b4318d98.s1.eu.hivemq.cloud")
            .serverPort(8883)
            .sslWithDefaultConfig()
            .buildAsync()


        // THIS WORKS!!
        client3TEST.connectWith()
            .simpleAuth()
            .username("row64")
            .password("temp7777".toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete({ connAck, throwable ->
                if (throwable != null) {
                    // handle failure
                    println("TEST - Failed to connect")
                    println(throwable.message)
                } else {
                    // setup subscribes or start publishing
                    println("Successfully connected")

                    // Send a single test message
                    client3TEST.publishWith()
                        .topic("TEST")
                        .payload("Message from testConnect_01".toByteArray())
                        .qos(MqttQos.EXACTLY_ONCE)
                        .send()
                        .whenCompleteAsync { publish, throwable ->
                            if (throwable != null) {
                                println("Could not send message")
                                println(throwable.message)
                            } else {
                                println("Sent message")
                            }
                        }
                }
            })

    }


// ********************************************************************





    // ------------------------------------------------------------
    // SUPPORTING METHODS
    // THESE DO NOT WORK! DELETE THESE LATER*****************************************************


    /**
     * Establish a connection with the server using an MQTT 3 client
     */
    private fun doConnect(client: Mqtt3AsyncClient) {

        // Prepare the connection for basic auth, if user/pass are present
        setBasicAuth(client)

        // Connect
        client.connectWith()
            .send()
            .whenComplete { ack, throwable ->
                if (throwable != null) {
                    println("Failed to connect")
                    println(throwable.message)
                }
                else {
                    println("Successfully connected")
                }
            }

    }


    /**
     * Establish a connection with the server using an MQTT 5 client
     */
    private fun doConnect(client: Mqtt5AsyncClient) {


        println("mqConnect for client 5 is not yet implemented...")

    }

    /**
     * Sets up the basic authentication for an MQTT 3 client.
     * Basic authentication consists of a username and a password.
     * Basic authentication is optional
     */
    private fun setBasicAuth(client: Mqtt3AsyncClient): Mqtt3AsyncClient {
        if (login.user != null && login.pass != null) {
            client.connectWith().simpleAuth()
                .username(login.user!!)
                .password(login.pass!!.toByteArray())
                .applySimpleAuth()
        }
        return client
    }


    /**
     * Override for an MQTT 5 client
     */
    private fun setBasicAuth(client: Mqtt5AsyncClient) {


    }





}
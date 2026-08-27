/**
 * Implementation of the HiveMQ Client library is based on the documentation:
 * https://hivemq.github.io/hivemq-mqtt-client/
 * https://www.hivemq.com/blog/mqtt-client-library-enyclopedia-hivemq-mqtt-client/
 */

package com.example.location_client_android

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
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

    // Async connection status variables
    lateinit private var connAck3: CompletableFuture<Mqtt3ConnAck>
    lateinit private var connAck5: CompletableFuture<Mqtt5ConnAck>

    // ---------------------------------------------------------------------------------------------
    // CONNECT METHODS

    /**
     * This is the primary connection method used.
     *
     * This method cansists of two supporting methods:
     *  - mqConnect5()
     *  - mqConnect3()
     *
     *  The HiveMQ Client library does not include automatic fallback features. So,
     *  if you try to use a v5 client and connect to a v3.1.1 broker, the connection
     *  will fail, and the client will not automatically fall back to v3.
     *
     *  To handle this, in this mqConnect() method, I first attempt to connect to a
     *  broker using the v5 client, calling the mqConnect5() client. If this connection
     *  attempt fails, then mqConnect() will attempt the same connection using the v3
     *  client, calling mqConnect3()
     *
     *  A truly failed connection occurs only if the client fails to connect to the broker
     *  with both version 5 and 3.1.1.
     */
    fun mqConnect(): Boolean {

        // How to identify a failed connection?
        // Does it throw an exception?

        // Maybe have the connect method throw a custom exception if it cannot connect,
        // triggering the attempt to try the next MQTT version

        /**
         * This is not working. I think it's because, in version 3, I'm trying
         * to throw an exception from an asynchronous function, when the main
         * thread has moved past it -?
         */

        try {
            mqConnect5()
        }
        catch (e: MqFailedConnection5Exception) {
            println("Caught MqFailedConnection5Exception")
            println("Attempting to connect with version 3.1.1...")

            try {
                mqConnect3()
            }
            catch (e: MqFailedConnection3Exception) {
                println("Caught MqFailedConnection3Exception")
                println("Total connection failure.")
                return false
            }

        }

        // Successful connection
        return true
    }



    private fun mqConnect3() {

        println("Got to mqConnect3()")

        println("Attempting to connect with version 3 using basic authentication...")

        client3.connectWith()
            .simpleAuth()
            .username(login.user!!)
            .password(login.pass!!.toByteArray())
            .applySimpleAuth()
            .send()
            .whenCompleteAsync { _, throwable ->
                // For failure
                if (throwable != null) {
                    println("Connection failed")
                    throw MqFailedConnection3Exception("")
                }
                // For success
                else {
                    println("Connection was successful")

                    // Enable send button
                    // ...

                }

            }




    }




    private fun mqConnect5() {
        // ...
        println("Error: Method 'mqConnect5' is not yet implemented...")
        throw MqFailedConnection5Exception("")
    }

    // ---------------------------------------------------------------------------------------------
    // PUBLISH METHODS


    // ...


    fun mqPublish3(topic: String, payload: ByteArray) {
        client3.publishWith()
            .topic(topic)
            .payload(payload)
            .qos(MqttQos.EXACTLY_ONCE)
            .send()
            .whenComplete { publish, throwable ->
                // For error
                if (throwable != null) {
                    println("Failed to send message")
                    println(throwable.message)
                }
                // For success
                else {
                    println("Message sent")
                }
            }
    }


    fun mqPublish5() {


        // ...

    }



    // ---------------------------------------------------------------------------------------------
    // RECEIVE METHODS

    // ...


    // ---------------------------------------------------------------------------------------------
    // DISCONNECT

    fun disconnectAll() {
        client3.disconnect()
        client5.disconnect()
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
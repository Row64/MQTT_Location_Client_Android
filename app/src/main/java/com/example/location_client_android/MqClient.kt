package com.example.location_client_android

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import java.util.UUID

class MqClient(val login: MqLogin) {

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


    /**
     * IMPLEMENTATION GOALS
     *
     *  - Test no auth
     *      - Hive server does not support no auth (no username/password)
     *  - Implement MQTT 5 connectivity
     *      - Include the ability to detect the broker's version
     *      - Connect with detected protocol
     *          - Or, try 5 first, if connection fails, try 3?
     *  - Implement advanced authentication
     *  - Implement additional features from library
     *
     */



    // Use a generic function to determine and return client according to highest supported version? ********************


    /**
     * Establishes a connection with the broker.
     *
     * Needs to be able to establish either a version 3 or 5 connection ...*********************
     */
    fun mqConnect() {

        println("ERROR: Method 'mqConnect' is not yet implemented.")

    }






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
     * Remove this function when finished
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
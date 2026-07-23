package com.example.mqtt_client_v4

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import java.util.UUID

class MqClient(val login: MqLogin) {

    // MQTT 3 client
    private val client3: Mqtt3AsyncClient = MqttClient.builder()
        .useMqttVersion3()
        .identifier(UUID.randomUUID().toString())
        .serverHost(login.host)
        .serverPort(login.port)
        .sslWithDefaultConfig()
        .buildAsync()

    // MQTT 5 client
    private val client5: Mqtt5AsyncClient = MqttClient.builder()
        .useMqttVersion5()
        .identifier(UUID.randomUUID().toString())
        .serverHost(login.host)
        .serverPort(login.port)
        .sslWithDefaultConfig()
        .buildAsync()






    // Use a generic function to determine and return client according to highest supported version? ********************


    /**
     * Establishes a connection with the broker.
     *
     * Needs to be able to establish either a version 3 or 5 connection ...*********************
     */
    fun mqConnect() {

        // Needs to be able to connect to either v5 or v3 brokers ****************************
        val targetClient = client3



        doConnect(targetClient)

    }


    /**
     * FOR TESTING ONLY! ****************************************************
     * Remove this function when finished
     */
    fun testConnect() {

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
                        .payload("Message from client3TEST".toByteArray())
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
    private fun setBasicAuth(client: Mqtt3AsyncClient) {
        if (login.user != null && login.pass != null) {
            client.connectWith().simpleAuth()
                .username(login.user!!)
                .password(login.pass!!.toByteArray())
                .applySimpleAuth()
        }
    }


    /**
     * Override for an MQTT 5 client
     */
    private fun setBasicAuth(client: Mqtt5AsyncClient) {


    }





}
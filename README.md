# Row64 Location Client

The Row64 Location Client for Android is an open-source Kotlin application that periodically queries the current location of an Android device and streams the coordinates to an MQTT broker.



## MQTT Versions

This application can connect to a broker using either version 5 or 3.1.1. When a user attempts to establish a connection to a broker, the application first attempts to establish a connection using MQTT 5. If the connection fails, the application automatically rolls back and attempts the connection using MQTT 3.1.1. A true connection failure occurs if a connection fails with both versions. Users do not need to specify the MQTT version in the application, as the application automatically identifies the highest compatible version with the targeted broker.



## Authentication Options

The application supports flexible username and password authentication options. This application currently supports the following authentication options:

* No authentication (anonymous authentication)
* Username-only authentication
* Basic authentication (username and password)

To connect to an MQTT broker, users must provide a valid host and port, with the port number being a positive integer within the range of 1 through 65,535.



## Location Permissions

This application requires fine location permissions to operate. If the user does not grant adequate permissions, the application will not work. When running the application for the first time, users will be prompted to grant permissions after connecting to a broker and attempting to send a location signal.

Application permissions can always be managed and updated in the Android system settings.



## Message Topic

This application sends MQTT messages with the topic: *R64_LOCATION_UPDATE*. To receive location updates to your broker, please ensure you are subscribed to this topic.


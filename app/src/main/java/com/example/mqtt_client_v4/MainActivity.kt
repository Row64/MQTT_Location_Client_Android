package com.example.mqtt_client_v4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mqtt_client_v4.ui.theme.MQTT_Client_v4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MQTT_Client_v4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        text = "Row64 MQTT Client Signal Generator",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            // --------------------------------------------
            // PROOF OF CONCEPT TEST

            val host: String = "5ab2c7f979c54060853470b0b4318d98.s1.eu.hivemq.cloud"
            val port: Int = 8883
            val user: String = "row64"
            val pass: String = "temp7777"

            val login = MqLogin(host, port, user, pass)

            val session = MqClient(login)

            session.testConnect()

            // --------------------------------------------

        }
    }
}




@Composable
fun Greeting(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier
    )
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MQTT_Client_v4Theme {
//        Greeting("Android")
//    }
//}
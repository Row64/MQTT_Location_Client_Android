/**
 * The MainActivity is the primary interface for the app.
 */

package com.example.location_client_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.location_client_android.ui.theme.Location_Client_AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Location_Client_AndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        text = "Row64 MQTT Client Signal Generator",
                        modifier = Modifier.padding(innerPadding)
                    )

                    BottomAppBarMain()
                    BtnLayout()

                }
            }
        }
    }
}



// -----------------------------------------------------------------------------------------------
// APP MAIN SCREEN

@Composable
fun Greeting(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier
    )
}

/**
 * Bottom App Bar
 * https://developer.android.com/develop/ui/compose/components/app-bars
 */
@Composable
fun BottomAppBarMain() {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                // Actions are a series of icons that appear on the left side of the bar.
                // Can be navigation items, or key action items for the screen.
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            tint = MaterialTheme.colorScheme.primary, // Applies dynamic theme color
                            contentDescription = "Home Button", // Essential for accessibility
                        )
                    }
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            Icons.Filled.Edit,
//                            contentDescription = "Localized description",
//                        )
//                    }
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            Icons.Filled.Mic,
//                            contentDescription = "Localized description",
//                        )
//                    }
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            Icons.Filled.Image,
//                            contentDescription = "Localized description",
//                        )
//                    }
                },
                floatingActionButton = {
//                    FloatingActionButton(
//                        onClick = { /* do something */ },
//                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
//                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
//                    ) {
//                        Icon(Icons.Filled.Add, "Localized description")
//                    }
                }
            )
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "Example of a scaffold with a bottom app bar."
        )
    }
}

/**
 * Navigation bar scroll items:
 * https://github.com/android/snippets/blob/311097c51d4087dd2baf6c3f378313270b7fb863/compose/snippets/src/main/java/com/example/compose/snippets/components/AppBar.kt#L385
 */
@Composable
fun ScrollContent(innerPadding: PaddingValues) {
    val range = 1..100

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(range.count()) { index ->
            Text(text = "- List item number ${index + 1}")
        }
    }
}

// -----------------------------------------------------------------------------------------------
// BUTTON TEST

/**
 * Button layout (?)
 * https://github.com/android/snippets/blob/50755e81ab2871f73ecdb762292d17895a483bff/compose/snippets/src/main/java/com/example/compose/snippets/components/Button.kt#L58-L63
 */
@Composable
fun BtnLayout() {
    Column(
        modifier = Modifier
            .padding(48.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        btnToLocationScreen(onClick = {
            // ...
        })
    }
}




/**
 * Takes the user to the Send Location screen.
 *
 * This specific implementation is for testing.
 */
@Composable
fun btnToLocationScreen(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("To Location Screen")
    }
}
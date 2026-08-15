package com.example.location_client_android

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.serialization.*
import kotlinx.serialization.json.*

// Fragment destinations (type safe)
// https://developer.android.com/guide/navigation/design/kotlin-dsl#routes
// https://developer.android.com/guide/navigation/design/type-safety
@Serializable
data object Home
@Serializable
data object Location

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // -----------------------------------------------------------------------------------
        /**
         * Establish fragment navigation using Kotlin DSL.
         *
         * First, create a reference to the NavHostFragment, then associate it to
         * an instance of the NavController.
         *
         * The NavHostFragment is hosted in res/xml/activity_main.xml
         * The id of the NavHostFragment is: nav_host_fragment
         *
         * https://developer.android.com/guide/navigation/navcontroller
         */

        // Create a reference to the NavHostFragment with ID nav_host_fragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Retrieve the NavController for the selected NavHostFragment
        val navController = navHostFragment.navController

        /**
         * Simultaneously create and add a navigation graph to the NavController
         * using the createGraph() method and add destinations.
         *
         * https://developer.android.com/guide/navigation/design#dsl-views
         *
         * https://developer.android.com/guide/navigation/design/kotlin-dsl#routes
         */

        navController.graph = navController.createGraph(
            startDestination = Home
        ) {
            fragment<HomeFragment, Home> {
//                label = resources.getString(R.string.layout_fragment_home)
            }
            fragment<LocationFragment, Location> {
//                label = resources.getString(R.string.layout_fragment_location)
            }
        }







        // Create an action bar based on the NavController
        // setupActionBarWithNavController(navController) THROWS EXCEPTION BEFORE GRAPH IS MADE

        // -----------------------------------------------------------------------------------


        // Layout XML
        // setContentView(R.layout.layout_fragment_home)





        // NAVIGATION BAR!!
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)



        // TEST - Switch fragments on startup
//        if (savedInstanceState == null) {
//            supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                add<LocationFragment>(R.id.fragment_container_view)
//            }
//        }

        /**
         * Intents for navigating between app Activities.
         * Activities are declared here, but called using startActivity() when buttons
         * are selected in the bottom app bar.
         *
         * If an activity also uses a service, you must start that service using
         * startService().
         *
         * Helpful resources:
         * https://developer.android.com/guide/components/intents-filters
         * https://stackoverflow.com/questions/9937120/switching-between-activities-in-android
         */
//        val locationIntent = Intent(this, LocationActivity::class.java)



        enableEdgeToEdge()
//        setContent {
//
//
//            /**
//             * Create the nav controller
//             * https://developer.android.com/guide/navigation/navcontroller#kotlin
//             */
//            val navController = rememberNavController()
//
//            Location_Client_AndroidTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        text = "Row64 MQTT Client Signal Generator",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//
//
//                    BottomAppBarMain(this)
//
//                    // For testing - automatically switch activities when app launches
////                    startActivity(locationIntent)
//
//
//
//                }
//            }
//
//        }


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
fun BottomAppBarMain(context: Context) {
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
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.login),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Login",
                        )
                    }
                    IconButton(onClick = {
//                        startActivity(
//                            context,
//                            Intent(context, LocationActivity::class.java),
//                            null)

                        // Switch fragment
                        // ...

                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.send_data),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Share Location",
                        )
                    }
                },
                // This app doesn't need a floating action button
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
    ) {
        innerPadding ->
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
 *
 * THIS BUTTON IS NO LONGER NEEDED
 */
//@Composable
//fun BtnLayout() {
//    Column(
//        modifier = Modifier
//            .padding(48.dp)
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        btnToLocationScreen(onClick = {
//            // ...
//        })
//    }
//}


/**
 * Takes the user to the Send Location screen.
 *
 * This specific implementation is for testing.
 *
 * THIS BUTTON IS NO LONGER NEEDED
 */
//@Composable
//fun btnToLocationScreen(onClick: () -> Unit) {
//    Button(onClick = { onClick() }) {
//        Text("To Location Screen")
//    }
//}
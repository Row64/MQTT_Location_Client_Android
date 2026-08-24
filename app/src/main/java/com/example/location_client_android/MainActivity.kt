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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 *  Fragment destinations (type safe)
 *  https://developer.android.com/guide/navigation/design/kotlin-dsl#routes
 *  https://developer.android.com/guide/navigation/design/type-safety
 */
//@Serializable
//data object Home
//@Serializable
//data object Location

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * The main activity's layout hosts the navigation fragment
         * and the navigation bar.
         */
        setContentView(R.layout.layout_activity_main)

        // -----------------------------------------------------------------------------------
        // FRAGMENT NAVIGATION ATTEMPT 2
        // Uses dedicated nav graph XML

        /**
         * Establish the NavHostFragment and the NavController.
         *
         * https://developer.android.com/guide/navigation/navcontroller
         *
         * This references the ID of the navigation host fragment, which is
         * hosted within the main activity's layout.
         */
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.findNavController()

        // Associate the NavController with the nav bar
        // https://developer.android.com/guide/navigation/integrations/ui#bottom_navigation
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)

        // -----------------------------------------------------------------------------------


//        // -----------------------------------------------------------------------------------
//        // FRAGMENT NAVIGATION ATTEMPT 1
//        // Attempt uses dynamic nav graph with programmatic approach
//        // Couldn't get to work
//
//        /**
//         * Establish fragment navigation using Kotlin DSL.
//         *
//         * First, create a reference to the NavHostFragment, then associate it to
//         * an instance of the NavController.
//         *
//         * The NavHostFragment is hosted in res/xml/layout_activity_main.xml
//         * The id of the NavHostFragment is: nav_host_fragment
//         *
//         * https://developer.android.com/guide/navigation/navcontroller
//         */
//
//        // Create a reference to the NavHostFragment with ID nav_host_fragment
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//
//        // Retrieve the NavController for the selected NavHostFragment
//        val navController = navHostFragment.navController
//
//        /**
//         * Simultaneously create and add a navigation graph to the NavController
//         * using the createGraph() method and add destinations.
//         *
//         * https://developer.android.com/guide/navigation/design#dsl-views
//         *
//         * https://developer.android.com/guide/navigation/design/kotlin-dsl#routes
//         */
//        navController.graph = navController.createGraph(
//            startDestination = Home
//        ) {
//            fragment<HomeFragment, Home>(R.id.destination_home)
//            fragment<LocationFragment, Location> {}
//        }
//
//        // Create an action bar based on the NavController
//        // https://developer.android.com/guide/navigation/integrations/ui#bottom_navigation
//        findViewById<BottomNavigationView>(R.id.bottom_navigation)
//            .setupWithNavController(navController)
//
//        // -----------------------------------------------------------------------------------



    }
}

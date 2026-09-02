package com.example.location_client_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * The main activity's layout hosts the navigation fragment
         * and the navigation bar.
         */
        setContentView(R.layout.layout_activity_main)

        // -----------------------------------------------------------------------------------
        // FRAGMENT NAVIGATION
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


    }

}

package com.example.myprofile.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.myprofile.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * The MainActivity class represents the main activity of the application.
 * It handles deep link processing and sets up navigation using NavHostFragment.
 *
 * @constructor Creates an instance of MainActivity.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Deep Link processing at Activity launch
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Deep Link processing when receiving a new intent
        handleDeepLink(intent)
    }

    /**
     * Handles deep links by navigating to the appropriate fragment if a valid deep link is detected.
     *
     * @param intent The intent containing the deep link data.
     */
    private fun handleDeepLink(intent: Intent?) {
        if (intent?.data != null) {
            // Obtain the NavController from the NavHostFragment
            val navController =
                (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment).navController
            // Navigate to the SearchFragment
            navController.navigate(R.id.searchFragment)
        }
    }
}

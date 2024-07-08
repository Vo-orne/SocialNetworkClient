package com.example.myprofile.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.myprofile.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * The MainActivity class represents the main activity of the application.
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

    private fun handleDeepLink(intent: Intent?) {
        if (intent?.data != null) {
            val navController =
                (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment).navController
            navController.navigate(R.id.searchFragment)
        }
    }

//    private fun handleDeepLink(intent: Intent) {
//        val action = intent.action
//        val data: Uri? = intent.data
//
//        if (Intent.ACTION_VIEW == action && data != null) {
//            val path = data.pathSegments
//
//            if (path.isNotEmpty()) {
//                when (path[0]) {
//                    "home" -> navigateToFragment(MyProfileFragment())
//                    "profile" -> navigateToFragment(MyContactsFragment())
//                }
//            }
//        }
//    }
//
//    private fun navigateToFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, fragment)
//            .addToBackStack(null)
//            .commit()
//    }
}
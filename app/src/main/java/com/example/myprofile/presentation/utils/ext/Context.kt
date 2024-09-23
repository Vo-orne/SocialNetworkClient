package com.example.myprofile.presentation.utils.ext

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Extension function to check internet connectivity.
 * @return Boolean indicating whether internet is available.
 */
fun Context.isInternetAvailable(): Boolean {
    // Get the ConnectivityManager system service
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Get the active network
    val activeNetwork = connectivityManager.activeNetwork ?: return false

    // Get the network capabilities of the active network
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    // Check if the network has transport capabilities (WiFi, Cellular, Ethernet)
    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}
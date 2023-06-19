package ru.kggm.core.presentation.utility.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.kggm.core.utility.classTag

private fun Context.getConnectivityManager() = ContextCompat.getSystemService(
    this,
    ConnectivityManager::class.java
) as ConnectivityManager

fun Fragment.registerNetworkCallback(
    callback: ConnectivityManager.NetworkCallback
) {
    requireContext().getConnectivityManager().registerDefaultNetworkCallback(callback)
}

fun Fragment.unregisterNetworkCallback(
    callback: ConnectivityManager.NetworkCallback
) {
    requireContext().getConnectivityManager().unregisterNetworkCallback(callback)
}

private val ALL_TRANSPORT_VARIANTS by lazy {
    listOf(
        NetworkCapabilities.TRANSPORT_BLUETOOTH,
        NetworkCapabilities.TRANSPORT_CELLULAR,
        NetworkCapabilities.TRANSPORT_ETHERNET,
        NetworkCapabilities.TRANSPORT_WIFI,
        NetworkCapabilities.TRANSPORT_VPN
    )
}

fun Fragment.getIsNetworkConnectionActive(): Boolean {
    val manager = requireContext().getConnectivityManager()
    val network = manager.activeNetwork ?: return false
    val capabilities = manager.getNetworkCapabilities(network) ?: return false
    return ALL_TRANSPORT_VARIANTS.any { capabilities.hasTransport(it) }
}

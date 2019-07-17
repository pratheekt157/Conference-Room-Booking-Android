@file:Suppress("DEPRECATION")

package com.example.conferencerommapp.Helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkState {
    companion object {
        fun appIsConnectedToInternet(mContext: Context): Boolean {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
    }
}
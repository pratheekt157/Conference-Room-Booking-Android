package com.example.conferencerommapp

import android.app.Application
import android.content.Context
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.example.conferencerommapp.utils.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import android.content.Intent
import com.example.conferencerommapp.Activity.SplashScreen
import com.example.conferencerommapp.Activity.UserBookingsDashboardActivity


class BaseApplication: Application(){
    override fun onCreate() {
        super.onCreate()
//        Log.e("log","Likh")
//        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
//            Crashlytics.logException(e)
//            Log.d("AppCrash", "Error just launched ")
//            handleUncaughtException(thread, e)
//        }
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE).edit().putString(
                    Constants.DEVICE_ID, task.result!!.token).apply()
            })

    }
//    private fun handleUncaughtException(thread: Thread, e: Throwable) {
//        val intent = Intent(applicationContext, AddConferenceRoom::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        System.exit(1)
//    }
}
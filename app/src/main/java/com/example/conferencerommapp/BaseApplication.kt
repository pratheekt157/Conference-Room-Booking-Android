package com.example.conferencerommapp

import android.app.Application
import android.content.Context
import com.example.conferencerommapp.Helper.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class BaseApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE).edit().putString(Constants.DEVICE_ID, task.result!!.token).apply()
            })
    }
}
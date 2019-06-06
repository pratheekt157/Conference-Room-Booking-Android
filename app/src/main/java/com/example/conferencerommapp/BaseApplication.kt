package com.example.conferencerommapp

import android.app.Application
import android.content.Context
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
                getSharedPreferences("myPref", Context.MODE_PRIVATE).edit().putString("deviceId", task.result!!.token).apply()
            })
    }
}
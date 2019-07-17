package com.example.conferencerommapp.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle


class ForegroundCounter: Application.ActivityLifecycleCallbacks{
    lateinit var instance: ForegroundCounter
    var startCount: Int = 0

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
        startCount++
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        startCount++
    }

    override fun onActivityStopped(activity: Activity?) {
        startCount--
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }


    fun createAndInstallCallbacks(application: Application): ForegroundCounter {
        val checker = ForegroundCounter()
        application.registerActivityLifecycleCallbacks(checker)
        return checker
    }

    fun inForeground(): Boolean {
        return startCount != 0
    }

}
package com.example.conferencerommapp.utils

import android.app.Activity
import android.view.WindowManager

class HideSoftKeyboard {
    companion object{
        public fun hideSoftKeyboard(activity: Activity) {
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }
}
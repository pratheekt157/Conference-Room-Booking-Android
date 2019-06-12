package com.example.conferencerommapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.conferencerommapp.Helper.GoogleGSO
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn

class ShowDialogForSessionExpired {
    companion object {
        /**
         * show dialog when session expired
         */
        fun showAlert(mConext: Context, activity: Activity) {
            val dialog = GetAleretDialog.getDialog(
                mConext,
                mConext.getString(R.string.session_expired), "Your session is expired!\n" +
                        mConext.getString(R.string.session_expired_messgae)
            )
            dialog.setPositiveButton(R.string.ok) { _, _ ->
                    signOut(mConext, activity)
            }
            val builder = GetAleretDialog.showDialog(dialog)
            ColorOfDialogButton.setColorOfDialogButton(builder)
        }

        /**
         * sign out from application
         */
        fun signOut(mContext: Context, activity: Activity) {
            val mGoogleSignInClient = GoogleGSO.getGoogleSignInClient(mContext)
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity) {
                    mContext.startActivity(Intent(mContext, SignIn::class.java))
                    (mContext as Activity).finish()
                }
        }
    }
}
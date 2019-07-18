package com.example.conferencerommapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.Html
import androidx.core.content.ContextCompat.startActivity
import com.example.conferencerommapp.SignIn
import com.google.android.gms.common.internal.ConnectionErrorMessages

class GetAleretDialog {
    companion object {
        fun getDialog(mContext: Context, title: String, message: String): AlertDialog.Builder {
            val mDialog = AlertDialog.Builder(mContext)
            mDialog.setTitle(title)
            mDialog.setMessage(message)
            return mDialog
        }

        fun showDialog(mDialog: AlertDialog.Builder): AlertDialog {
            val dialog: AlertDialog = mDialog.create()
            dialog.show()
            dialog.setCanceledOnTouchOutside(false)
            return dialog
        }

        fun getDialogforRecurring(mContext: Context,title: String):AlertDialog.Builder{
            val mDialog = AlertDialog.Builder(mContext)
            mDialog.setTitle(title)

            return mDialog
        }

        fun getDialogForPasscode(mContext: Context,title: String,passcode: String):AlertDialog.Builder{
            val mDialog = AlertDialog.Builder(mContext)
            mDialog.setTitle(title)
            mDialog.setMessage(Html.fromHtml("Your passcode is " +"<b>"+ passcode + "</b>"+". You can use this passcode to book a room from tablet placed inside conference room."))
            return mDialog
        }


    }
}
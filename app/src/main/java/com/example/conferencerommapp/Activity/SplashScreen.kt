package com.example.conferencerommapp.Activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.utils.GetProgress
import com.example.conferencerommapp.Helper.NetworkState
import com.example.conferencerommapp.utils.ShowToast
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.CheckRegistrationViewModel
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn


class SplashScreen : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mCheckRegistrationViewModel: CheckRegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        init()
        observeData()
        val logoHandler = Handler()
        val logoRunnable = Runnable {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                if(NetworkState.appIsConnectedToInternet(this)) {
                    checkRegistration()
                } else {
                    val i = Intent(this@SplashScreen, NoInternetConnectionActivity::class.java)
                    startActivityForResult(i, Constants.RES_CODE)
                }
            } else {
                signIn()
            }
        }
        logoHandler.postDelayed(logoRunnable, 3000)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            checkRegistration()
        }
    }

    /**
     * function make a request to backend for checking whether the user is registered or not
     */
    private fun checkRegistration() {
        progressDialog.show()
        mCheckRegistrationViewModel.checkRegistration(GetPreference.getTokenFromPreference(this), GetPreference.getDeviceIdFromPreference(this))
    }

    /**
     * initialize all lateinit variables
     */
    fun init() {
        progressDialog =  GetProgress.getProgressDialog(getString(R.string.progress_message), this)
        prefs = getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE)
        mCheckRegistrationViewModel = ViewModelProviders.of(this).get(CheckRegistrationViewModel::class.java)
    }

    private fun observeData() {
        mCheckRegistrationViewModel.returnSuccessCode().observe(this, Observer {
            progressDialog.dismiss()
            setValueForSharedPreference(it)
        })
        mCheckRegistrationViewModel.returnFailureCode().observe(this, Observer {
            progressDialog.dismiss()
            if(it == Constants.INVALID_TOKEN) {
                signIn()
                finish()
            }else {
                Toast.makeText(this, "" + it, Toast.LENGTH_SHORT).show()
                ShowToast.show(this, it as Int)
                finish()
            }
        })
    }
    /**
     * pass the intent for the SignIn Activity
     */
    private fun signIn() {
        startActivity(Intent(applicationContext, SignIn::class.java))
        finish()
    }

    /**
     * according to the backend status function will redirect control to some other activity
     */
    private fun goToNextActivity(code: Int?) {
        when (code) {
            Constants.HR_CODE, Constants.Facility_Manager, Constants.MANAGER_CODE, Constants.EMPLOYEE_CODE -> {
                startActivity(Intent(this@SplashScreen, UserBookingsDashboardActivity::class.java))
                finish()
            }
            else -> {
                val builder = AlertDialog.Builder(this@SplashScreen)
                builder.setTitle(getString(R.string.error))
                builder.setMessage(getString(R.string.restart_app))
                builder.setPositiveButton(getString(R.string.ok)) { _,_ ->
                    finish()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    /**
     * set value in shared preference
     */
    private fun setValueForSharedPreference(it : com.example.conferencerommapp.Model.SignIn) {
        val editor = prefs.edit()
        val code : String = it.StatusCode.toString()
        editor.putInt(Constants.ROLE_CODE,code.toInt())
        editor.apply()
        goToNextActivity(code.toInt())
    }
}

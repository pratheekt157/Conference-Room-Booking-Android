package com.example.conferencerommapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.Activity.NoInternetConnectionActivity
import com.example.conferencerommapp.Activity.UserBookingsDashboardActivity
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.ViewModel.CheckRegistrationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SignIn : AppCompatActivity() {

    private var RC_SIGN_IN = 0
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var prefs: SharedPreferences
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mCheckRegistrationViewModel: CheckRegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_new)
        ButterKnife.bind(this)
        initialize()
        observeData()
    }
    @OnClick(R.id.sign_in_button)
    fun signIn() {
        startIntentToGoogleSignIn()
    }

    /**
     * function intialize all items of UI, sharedPreference and calls the setAnimationToLayout function to set the animation to the layouts
     */
    fun initialize() {
        prefs = getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE)
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message_processing), this)
        mCheckRegistrationViewModel = ViewModelProviders.of(this).get(CheckRegistrationViewModel::class.java)
        initializeGoogleSignIn()
    }

    /**
     * function will starts a explict intent for the google sign in
     */
    private fun startIntentToGoogleSignIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * function will initialize the GoogleSignInClient
     */
    private fun initializeGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.server_client_id))
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    /**
     * function will automatically invoked once the control will return from the explict intent and than call another
     * method to do further task
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            //Toast.makeText(this, "" + data!!.getBooleanExtra("result", false), Toast.LENGTH_SHORT).show()
            checkRegistration()
        }
    }

    /**
     * function will call a another function which connects to the backend.
     */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            saveTokenAndUserIdInSharedPreference(account!!.idToken)
            if(NetworkState.appIsConnectedToInternet(this)) {
                checkRegistration()
            } else {
                val i = Intent(this@SignIn, NoInternetConnectionActivity::class.java)
                startActivityForResult(i, Constants.RES_CODE)
            }
        } catch (e: ApiException) {
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.statusCode)
        }

    }


    /**
     * Sign out from application
     */
    private fun signOut() {
        var mGoogleSignInClient = GoogleGSO.getGoogleSignInClient(this)
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
            }
    }


    private fun saveTokenAndUserIdInSharedPreference(idToken: String?) {
        val editor = prefs.edit()
        editor.putString("Token", idToken)
        editor.apply()
    }

    /**
     * on back pressed the function will clear the activity stack and will close the application
     */
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    /**
     * this function will check whether the user is registered or not
     * if not registered than make an intent to registration activity
     */
    private fun checkRegistration() {
        progressDialog.show()
        mCheckRegistrationViewModel.checkRegistration(getTokenFromPreference(), getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("deviceId", "Not Set")!!)
    }


    /**
     * observe data from server
     */
    private fun observeData() {
        //positive response from server
        mCheckRegistrationViewModel.returnSuccessCode().observe(this, Observer {
            progressDialog.dismiss()
            setValueForSharedPreference(it)
        })
        // Negative response from server
        mCheckRegistrationViewModel.returnFailureCode().observe(this, Observer {
            progressDialog.dismiss()
            ShowToast.show(this, it as Int)
            signOut()
        })
    }

    /**
     * this function will intent to some activity according to the received data from backend
     */
    private fun intentToNextActivity(code: Int?) {
        Log.i("---------", "" + code)
        when (code) {
            11, 10, 12, 13 -> {
                startActivity(Intent(this@SignIn, UserBookingsDashboardActivity::class.java))
                finish()
            }
            else -> {
                val builder =
                    GetAleretDialog.getDialog(this, getString(R.string.error), getString(R.string.restart_app))
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                    finish()
                }
                GetAleretDialog.showDialog(builder)
            }
        }
    }

    /**
     * a function which will set the value in shared preference
     */
    private fun setValueForSharedPreference(status: Int) {
        val code = status
        val editor = prefs.edit()
        editor.putInt("Code", code)
        editor.apply()
        intentToNextActivity(code)
    }

    /**
     * get token and userId from local storage
     */
    private fun getTokenFromPreference(): String {
        return getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("Token", "Not Set")!!
    }

}

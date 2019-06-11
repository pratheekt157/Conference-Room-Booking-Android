package com.example.conferencerommapp.Activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.BookingDashboardViewModel
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_user_dashboard.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.nav_header_main2.view.*
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class UserBookingsDashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var mBookingDashBoardViewModel: BookingDashboardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setNavigationViewItem()
        init()
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> selectedFragment = UpcomingBookingFragment()
                R.id.nav_previous -> selectedFragment = PreviousBookingFragment()
                R.id.nav_cancelled -> selectedFragment = CancelledBookingFragment()
                R.id.nav_new_booking -> selectedFragment = InputDetailsForBookingFragment()

            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    selectedFragment!!
                ).commit()
            }
            true
        }
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                UpcomingBookingFragment()
            ).commit()
        }
    }

    private fun init() {
        mProgressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message), this)
        mBookingDashBoardViewModel = ViewModelProviders.of(this).get(BookingDashboardViewModel::class.java)
        observeData()
    }

    private fun getPasscode() {
        mProgressDialog.show()
        mBookingDashBoardViewModel.getPasscode(GetPreference.getTokenFromPreference(this))
    }

    /**
     * all observer for LiveData
     */
    private fun observeData() {

        /**
         * observing data for booking list
         */
        mBookingDashBoardViewModel.returnPasscode().observe(this, androidx.lifecycle.Observer {
            mProgressDialog.dismiss()
            showAlertForPasscode(it)
        })
        mBookingDashBoardViewModel.returnPasscodeFailed().observe(this, androidx.lifecycle.Observer {
            mProgressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
            }
        })
    }


    /**
     * show dialog for passcode
     */
    private fun showAlertForPasscode(passcode: Int) {
        val dialog = GetAleretDialog.getDialog(
            this,
            getString(R.string.do_not_share),
            "Your passcode is " + passcode + ". You can use this passcode to book a room from tablet placed inside conference room."
        )
        dialog.setPositiveButton(R.string.ok) { _, _ ->
        }
        val builder = GetAleretDialog.showDialog(dialog)
        ColorOfDialogButton.setColorOfDialogButton(builder)
    }

    /**
     * show dialog for session expired
     */
    private fun showAlert() {
        val dialog = GetAleretDialog.getDialog(
            this, getString(R.string.session_expired), "Your session is expired!\n" +
                    getString(R.string.session_expired_messgae)
        )
        dialog.setPositiveButton(R.string.ok) { _, _ ->
            signOut()
        }
        val builder = GetAleretDialog.showDialog(dialog)
        ColorOfDialogButton.setColorOfDialogButton(builder)
    }

    /**
     * this function will set action to the item in navigation drawer like HR or Logout or Project Manager
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                signOut()
            }
            R.id.HR -> {
                startActivity(Intent(this@UserBookingsDashboardActivity, BlockedDashboard::class.java))
            }
            R.id.project_manager -> {
                startActivity(Intent(this@UserBookingsDashboardActivity, NewProjectManagerInput::class.java))
            }
            R.id.hr_add -> {
                startActivity(Intent(this@UserBookingsDashboardActivity, BuildingDashboard::class.java))
            }
            R.id.get_passcode -> {
                getPasscode()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * this function will set item in navigation drawer according to the data stored in the sharedpreference
     * if the code is 11 than the role is HR
     * if the code is 12 than the role is Project manager
     * else the role is normal user
     */
    private fun setItemInDrawerByRole() {
        val pref = getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE)
        val code = pref.getInt("Code", Constants.EMPLOYEE_CODE)
        val navMenu = nav_view.menu
        if (code != Constants.HR_CODE) {

        }
        if (code != Constants.MANAGER_CODE) {
            navMenu.findItem(R.id.project_manager).isVisible = false
        }
        if (code != Constants.Facility_Manager) {
            navMenu.findItem(R.id.HR).isVisible = false
            navMenu.findItem(R.id.hr_add).isVisible = false
        }
    }


    /**
     * this function will set items in the navigation view and calls another function for setting the item according to role
     * if there is no image at the particular url provided by google than we will set a dummy imgae
     * else we set the image provided by google and set the employeeList according to the google display employeeList
     */
    @SuppressLint("SetTextI18n")
    fun setNavigationViewItem() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        val viewH = nav_view.getHeaderView(0)
        val acct = GoogleSignIn.getLastSignedInAccount(this@UserBookingsDashboardActivity)
        viewH.nv_profile_name.text = "Hello, ${acct!!.displayName}"
        val personPhoto = acct.photoUrl
        viewH.nv_profile_email.text = acct.email
        Glide.with(applicationContext).load(personPhoto).thumbnail(1.0f).into(viewH.nv_profile_image)
        setItemInDrawerByRole()
    }

    /**
     * Sign out from application
     */
    private fun signOut() {
        var mGoogleSignInClient: GoogleSignInClient = GoogleGSO.getGoogleSignInClient(this)
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                Toasty.info(this, getString(R.string.successfully_sign_out), Toasty.LENGTH_SHORT).show()
                startActivity(Intent(this@UserBookingsDashboardActivity, SignIn::class.java))
                finish()
            }
    }

    //clear activity stack
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}




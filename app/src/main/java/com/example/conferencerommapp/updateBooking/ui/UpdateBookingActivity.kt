@file:Suppress("DEPRECATION")

package com.example.conferencerommapp.updateBooking.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.BaseApplication
import com.example.conferencerommapp.Helper.NetworkState
import com.example.conferencerommapp.Model.GetIntentDataFromActvity
import com.example.conferencerommapp.Model.UpdateBooking
import com.example.conferencerommapp.R
import com.example.conferencerommapp.checkConnection.NoInternetConnectionActivity
import com.example.conferencerommapp.updateBooking.repository.UpdateBookingRepository
import com.example.conferencerommapp.updateBooking.viewModel.UpdateBookingViewModel
import com.example.conferencerommapp.utils.*
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.analytics.FirebaseAnalytics
import es.dmoral.toasty.Toasty
import javax.inject.Inject

class UpdateBookingActivity : AppCompatActivity() {

    @Inject
    lateinit var mUpdateBookingRepo: UpdateBookingRepository

    private lateinit var mUpdateBookingViewModel: UpdateBookingViewModel
    private var mUpdateBooking = UpdateBooking()
    lateinit var mFirebaseAnalytics: FirebaseAnalytics

    @BindView(R.id.Purpose)
    lateinit var purpose: EditText

    @BindView(R.id.fromTime_update)
    lateinit var newFromTime: EditText

    @BindView(R.id.toTime_update)
    lateinit var newToTime: EditText

    @BindView(R.id.date_update)
    lateinit var date: EditText

    @BindView(R.id.buildingname)
    lateinit var buildingName: EditText

    @BindView(R.id.conferenceRoomName)
    lateinit var roomName: EditText

    private lateinit var progressDialog: ProgressDialog

    private lateinit var mIntentDataFromActivity: GetIntentDataFromActvity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_booking)
        ButterKnife.bind(this)
        mIntentDataFromActivity = getIntentData()
        init()
        observerData()
        setValuesInEditText(mIntentDataFromActivity)
        setEditTextPicker()
    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title = Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.update) + "</font>")
    }

    private fun addDataToObjects(mIntentDataFromActivity: GetIntentDataFromActvity) {
        mUpdateBooking.bookingId = mIntentDataFromActivity.bookingId
        mUpdateBooking.newFromTime =
            FormatTimeAccordingToZone.formatDateAsUTC(mIntentDataFromActivity.date + " " + newFromTime.text.toString().trim())
        mUpdateBooking.newtotime =
            (FormatTimeAccordingToZone.formatDateAsUTC(mIntentDataFromActivity.date + " " + newToTime.text.toString().trim()))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            addDataToObjects(mIntentDataFromActivity)
            validationOnDataEnteredByUser()
        }
    }

    @OnClick(R.id.update)
    fun updateMeeting() {
        if (NetworkState.appIsConnectedToInternet(this)) {
            addDataToObjects(mIntentDataFromActivity)
            validationOnDataEnteredByUser()
        } else {
            val i = Intent(this, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
    }

    private fun validationOnDataEnteredByUser() {
        /**
         * Validate each input field whether they are empty or not
         * If the field contains no values we show a toast to user saying that the value is invalid for particular field
         */
        if (validateFromTimeEditText() || validateToTimeEditText()) {
            val minMilliseconds: Long = Constants.MIN_MEETING_DURATION

            /**
             * Get the start and end time of meeting from the input fields
             */
            val startTime = newFromTime.text.toString()
            val endTime = newToTime.text.toString()

            /**
             * setting a aalert dialog instance for the current context
             */
            try {

                /**
                 * getting the values for time validation variables from method calculateTimeInMillis
                 */
                val (elapsed, elapsed2) = ConvertTimeInMillis.calculateTimeInMilliseconds(
                    startTime,
                    endTime,
                    mIntentDataFromActivity.date.toString()
                )
                /**
                 * if the elapsed2 < 0 that means the from time is less than the current time. In that case
                 * we restrict the user to move forword and show some message in alert that the time is not valid
                 */

                when {
                    elapsed2 < 0 -> {
                        val builder = GetAleretDialog.getDialog(
                            this,
                            getString(R.string.invalid),
                            getString(R.string.invalid_fromtime)
                        )
                        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                        }
                        val alertDialog = GetAleretDialog.showDialog(builder)
                        ColorOfDialogButton.setColorOfDialogButton(alertDialog)
                    }
                    minMilliseconds <= elapsed -> {
                        updateMeetingDetails()
                        updateBookingLogFirebaseAnalytics()
                    }
                    else -> {
                        val builder = GetAleretDialog.getDialog(
                            this,
                            getString(R.string.invalid),
                            getString(R.string.time_validation_message)
                        )

                        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                        }
                        GetAleretDialog.showDialog(builder)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@UpdateBookingActivity, getString(R.string.details_invalid), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun updateBookingLogFirebaseAnalytics() {
        val update = Bundle()
        mFirebaseAnalytics.logEvent(getString(R.string.update_log), update)
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true)
        mFirebaseAnalytics.setMinimumSessionDuration(5000)
        mFirebaseAnalytics.setSessionTimeoutDuration(1000000)
        mFirebaseAnalytics.setUserId(GoogleSignIn.getLastSignedInAccount(this)!!.email)
        mFirebaseAnalytics.setUserProperty(
            getString(R.string.Roll_Id),
            GetPreference.getRoleIdFromPreference(this).toString()
        )
    }

    private fun updateMeetingDetails() {
        progressDialog.show()
        mUpdateBookingViewModel.updateBookingDetails(mUpdateBooking, GetPreference.getTokenFromPreference(this))
    }

    /**
     * initialize all lateinit variables
     */
    fun init() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        initActionBar()
        initComponentForUpdateBooking()
        initLateInitializerVariables()
        initUpdateBookingRepo()

    }

    private  fun initComponentForUpdateBooking() {
        (application  as BaseApplication).getmAppComponent()?.inject(this)
    }

    private fun initUpdateBookingRepo() {
        mUpdateBookingViewModel.setUpdateBookingRepo(mUpdateBookingRepo)
    }


    private fun initLateInitializerVariables() {
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message_processing), this)
        mUpdateBookingViewModel = ViewModelProviders.of(this).get(UpdateBookingViewModel::class.java)
    }

    /**
     * observing data for update booking
     */
    private fun observerData() {
        mUpdateBookingViewModel.returnBookingUpdated().observe(this, Observer {
            progressDialog.dismiss()
            Toasty.success(this, getString(R.string.booking_updated), Toast.LENGTH_SHORT, true).show()
            finish()
        })
        mUpdateBookingViewModel.returnUpdateFailed().observe(this, Observer {
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                ShowDialogForSessionExpired.showAlert(this, UpdateBookingActivity())
            } else {
                ShowToast.show(this, it as Int)
            }
        })
    }


    private fun validateFromTimeEditText(): Boolean {
        return if (TextUtils.isEmpty(newFromTime.text.trim())) {
            Toast.makeText(applicationContext, getString(R.string.invalid_from_time), Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun validateToTimeEditText(): Boolean {
        return if (TextUtils.isEmpty(newToTime.text.trim())) {
            Toast.makeText(applicationContext, getString(R.string.invalid_to_time), Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun setEditTextPicker() {
        newFromTime.setOnClickListener {
            DateAndTimePicker.getTimePickerDialog(this, newFromTime)
        }

        /**
         * set Time picker for the edittext toTime
         */
        newToTime.setOnClickListener {
            DateAndTimePicker.getTimePickerDialog(this, newToTime)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setValuesInEditText(mIntentDataFromActivity: GetIntentDataFromActvity) {
        purpose.text = mIntentDataFromActivity.purpose!!.toEditable()


        newFromTime.text = mIntentDataFromActivity.fromTime!!.toEditable()
        newToTime.text = mIntentDataFromActivity.toTime!!.toEditable()
        date.text = FormatDate.formatDate(mIntentDataFromActivity.date!!).toEditable()

        buildingName.text = mIntentDataFromActivity.buildingName!!.toEditable()
        roomName.text = mIntentDataFromActivity.roomName!!.toEditable()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun getIntentData(): GetIntentDataFromActvity {
        return intent.extras!!.get(Constants.EXTRA_INTENT_DATA) as GetIntentDataFromActvity
    }

}

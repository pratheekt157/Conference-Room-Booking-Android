package com.example.conferencerommapp.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html.fromHtml
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.Model.Building
import com.example.conferencerommapp.Model.GetIntentDataFromActvity
import com.example.conferencerommapp.Model.RoomDetails
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.BuildingViewModel
import com.example.conferencerommapp.ViewModel.ConferenceRoomViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_booking_input_from_user.*
import kotlinx.android.synthetic.main.activity_project_manager_input.*

class InputDetailsForBooking : AppCompatActivity() {
    @BindView(R.id.date)
    lateinit var dateEditText: EditText
    @BindView(R.id.fromTime)
    lateinit var fromTimeEditText: EditText
    @BindView(R.id.toTime)
    lateinit var toTimeEditText: EditText
    @BindView(R.id.room_capacity)
    lateinit var roomCapacityEditText: EditText
    private lateinit var customAdapter: RoomAdapter
    private lateinit var mBuildingsViewModel: BuildingViewModel
    @BindView(R.id.recycler_view_room_list)
    lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.event_name_text_view)
    lateinit var purposeEditText: EditText
    @BindView(R.id.booking_scroll_view)
    lateinit var scroolView: NestedScrollView
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var mSetDataFromActivity: GetIntentDataFromActvity
    private lateinit var mConferenceRoomViewModel: ConferenceRoomViewModel
    private var mInputDetailsForRoom = InputDetailsForRoom()
    private var mSuggestedRoomApiIsCallled = false
    private lateinit var acct: GoogleSignInAccount
    var mSetIntentData = GetIntentDataFromActvity()
    var mBuildingName = "Select Building"
    var mBuildingId = -1
    var capacity = 0
    var buildingId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_input_from_user)
        ButterKnife.bind(this)
        init()
        observeData()
        search_room.setOnClickListener {
            suggestions.visibility = View.GONE
            validationOnDataEnteredByUser()
        }
    }

    private fun init() {
        val actionBar = supportActionBar
        actionBar!!.title =
            fromHtml("<font font-size = \"23px\" color=\"#FFFFFF\">" + getString(R.string.Booking_Details) + "</font>")
        setPickerToEditText()
        textChangeListenerOnDateEditText()
        textChangeListenerOnFromTimeEditText()
        textChangeListenerOnToTimeEditText()
        textChangeListenerOnPurposeEditText()
        textChangeListenerOnRoomCapacity()
        acct = GoogleSignIn.getLastSignedInAccount(this)!!
        mInputDetailsForRoom.email = acct.email.toString()
        mProgressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message), this)
        mBuildingsViewModel = ViewModelProviders.of(this).get(BuildingViewModel::class.java)
        mConferenceRoomViewModel = ViewModelProviders.of(this).get(ConferenceRoomViewModel::class.java)
        mSetDataFromActivity = GetIntentDataFromActvity()
        if (NetworkState.appIsConnectedToInternet(this)) {
            getViewModelForBuildingList()
        } else {
            val i = Intent(this, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
    }

    /**
     * get the object of ViewModel using ViewModelProviders and observers the data from backend
     */
    private fun getViewModelForBuildingList() {
        mProgressDialog.show()
        // make api call
        mBuildingsViewModel.getBuildingList(getTokenFromPreference())
    }

    private fun getViewModelForConferenceRoomList(mInputDetailsForRoom: InputDetailsForRoom) {
        mProgressDialog.show()
        mConferenceRoomViewModel.getConferenceRoomList(getTokenFromPreference(), mInputDetailsForRoom)
    }

    private fun setAdapter(mListOfRooms: List<RoomDetails>) {
        mProgressDialog.dismiss()
        customAdapter =
            RoomAdapter(mListOfRooms as ArrayList<RoomDetails>, this, object : RoomAdapter.ItemClickListener {
                override fun onItemClick(roomId: Int?, buidingId: Int?, roomName: String?, buildingName: String?) {
                    mSetIntentData.buildingName = mBuildingName
                    mSetIntentData.roomName = roomName
                    mSetIntentData.buildingId = mBuildingId
                    mSetIntentData.roomId = roomId
                    mSetIntentData.capacity = roomCapacityEditText.text.toString()
                    mSetIntentData.date = dateEditText.text.toString()
                    goToSelectMeetingMembersActivity()
                }
            })
        mRecyclerView.adapter = customAdapter
    }

    /**
     * observe data from server
     */
    private fun observeData() {
        mBuildingsViewModel.returnMBuildingSuccess().observe(this, Observer {
            mProgressDialog.dismiss()
            setBuildingSpinner(it)
        })
        mBuildingsViewModel.returnMBuildingFailure().observe(this, Observer {
            mProgressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
                finish()
            }
        })

        //positive response
        mConferenceRoomViewModel.returnSuccess().observe(this, Observer {
            checkForStatusOfRooms(it)
        })
        // Negative response
        mConferenceRoomViewModel.returnFailure().observe(this, Observer {
            mProgressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
                finish()
            }
        })

        // positive response for suggested rooms
        mConferenceRoomViewModel.returnSuccessForSuggested().observe(this, Observer {
            horizontal_line_below_search_button.visibility = View.VISIBLE
            suggestions.visibility = View.VISIBLE
            if(it.isEmpty()) {
                suggestions.text = "No Rooms Available"
            } else {
                suggestions.text = "No Room Available.Have look on suggestions"
            }
            setAdapter(it)
        })
        // negative response for suggested rooms
        mConferenceRoomViewModel.returnFailureForSuggestedRooms().observe(this, Observer {
            mProgressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
                finish()
            }
        })
    }
    private fun goToSelectMeetingMembersActivity() {
        mSetIntentData.fromTime = dateEditText.text.toString() + " " + fromTimeEditText.text.toString()
        mSetIntentData.toTime = dateEditText.text.toString() + " " + toTimeEditText.text.toString()
        mSetIntentData.purpose = purposeEditText.text.toString()
        var intent = Intent(this@InputDetailsForBooking, SelectMeetingMembersActivity::class.java)
        intent.putExtra(Constants.EXTRA_INTENT_DATA, mSetIntentData)
        startActivity(intent)
    }

    private fun checkForStatusOfRooms(mListOfRooms: List<RoomDetails>?) {
        var count = 0
        for (room in mListOfRooms!!) {
            if (room.status == "Unavailable") {
                count++
            }
        }
        if (count == mListOfRooms.size) {
            if (NetworkState.appIsConnectedToInternet(this)) {
                makeCallToApiForSuggestedRooms()
            } else {
                val i = Intent(this, NoInternetConnectionActivity::class.java)
                startActivityForResult(i, Constants.RES_CODE3)
            }

        } else {
            setAdapter(mListOfRooms)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            getViewModelForBuildingList()
        }
        if (requestCode == Constants.RES_CODE2 && resultCode == Activity.RESULT_OK) {

        }
        if (requestCode == Constants.RES_CODE3 && resultCode == Activity.RESULT_OK) {
            makeCallToApiForSuggestedRooms()
        }
    }
    private fun makeCallToApiForSuggestedRooms() {
        mSuggestedRoomApiIsCallled = true
        mConferenceRoomViewModel.getSuggestedConferenceRoomList(getTokenFromPreference(), mInputDetailsForRoom)
    }

    private fun setBuildingSpinner(mBuildingList: List<Building>) {
        var buildingNameList = mutableListOf<String>()
        var buildingIdList = mutableListOf<Int>()

        buildingNameList.add("Select Building")
        buildingIdList.add(-1)
        for (mBuilding in mBuildingList) {
            buildingNameList.add(mBuilding.buildingName!!)
            buildingIdList.add(mBuilding.buildingId!!.toInt())
        }
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_icon, R.id.spinner_text, buildingNameList)
        building_name_spinner.adapter = adapter
        building_name_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                mBuildingName = buildingNameList[position]
                mBuildingId = buildingIdList[position]
                text_view_error_spinner_building.visibility = View.INVISIBLE
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                mBuildingName = "Select Building"
            }
        }
    }

    /**
     * function will attach date and time picker to the input fields
     */
    private fun setPickerToEditText() {

        /**
         * set Time picker for the edittext fromtime
         */
        fromTimeEditText.setOnClickListener {
            DateAndTimePicker.getTimePickerDialog(this, fromTimeEditText)
        }

        /**
         * set Time picker for the edittext toTime
         */
        toTimeEditText.setOnClickListener {
            DateAndTimePicker.getTimePickerDialog(this, toTimeEditText)
        }
        /**
         * set Date picker for the edittext dateEditText
         */
        dateEditText.setOnClickListener {
            DateAndTimePicker.getDatePickerDialog(this, dateEditText)
        }
    }


    /**
     * add text change listener for the from time edit text
     */
    private fun textChangeListenerOnFromTimeEditText() {
        fromTimeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFromTime()
            }
        })
    }

    /**
     * add text change listener for the to time edit text
     */
    private fun textChangeListenerOnToTimeEditText() {
        toTimeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateToTime()
            }
        })
    }

    /**
     * add text change listener for the date edit text
     */
    private fun textChangeListenerOnDateEditText() {
        dateEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateDate()
            }
        })
    }

    /**
     * add text change listener for the purpose edit text
     */
    private fun textChangeListenerOnPurposeEditText() {
        purposeEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePurpose()
            }
        })
    }

    /**
     * add text change listener for the room name
     */
    private fun textChangeListenerOnRoomCapacity() {
        roomCapacityEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateRoomCapacity()
            }
        })
    }

    /**
     * validation for spinner
     */
    private fun validateRoomCapacity(): Boolean {
        return if (roomCapacityEditText.text.toString().trim().isEmpty()) {
            capacity_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            val input = roomCapacityEditText.text.toString().toLong()
            if (input <= 0 || input > Int.MAX_VALUE) {
                capacity_layout.error = getString(R.string.room_capacity_must_be_more_than_0)
                false
            } else {
                capacity_layout.error = null
                true
            }
        }
    }

    /**
     * validate all input fields
     */
    private fun validatePurpose(): Boolean {
        return if (purposeEditText.text.toString().trim().isEmpty()) {
            purpose_layout.error = getString(R.string.field_cant_be_empty)
            false
        }else {
            purpose_layout.error = null
            true
        }
    }

    /**
     * validations for all input fields for empty condition
     */
    private fun validateFromTime(): Boolean {
        val input = fromTimeEditText.text.toString().trim()
        return if (input.isEmpty()) {
            from_time_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            from_time_layout.error = null
            true
        }
    }

    private fun validateToTime(): Boolean {
        val input = toTimeEditText.text.toString().trim()
        return if (input.isEmpty()) {
            to_time_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            to_time_layout.error = null
            true
        }
    }

    private fun validateDate(): Boolean {
        val input = dateEditText.text.toString().trim()
        return if (input.isEmpty()) {
            date_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            date_layout.error = null
            true
        }
    }

    /**
     * validate building spinner
     */
    private fun validateBuildingSpinner(): Boolean {
        return if (mBuildingName == "Select Building") {
            text_view_error_spinner_building.visibility = View.VISIBLE
            false
        } else {
            text_view_error_spinner_building.visibility = View.INVISIBLE
            true
        }
    }

    /**
     * check validation for all input fields
     */
    private fun validate(): Boolean {

        if (!validateFromTime() or !validateToTime() or !validateDate() or !validateBuildingSpinner() or !validatePurpose() or !validateRoomCapacity()) {
            return false
        }
        return true
    }


    /**
     * function will apply some validation on data entered by user
     */
    private fun validationOnDataEnteredByUser() {

        /**
         * Validate each input field whether they are empty or not
         * If the field contains no values we show a toast to user saying that the value is invalid for particular field
         */
        if (validate()) {
            validateTime(fromTimeEditText.text.toString(), toTimeEditText.text.toString())
        }
    }


    private fun validateTime(startTime: String, endTime: String) {
        val minMilliseconds: Long = 600000
        /**
         * setting a alert dialog instance for the current context
         */
        try {

            /**
             * getting the values for time validation variables from method calculateTimeInMillis
             */
            val (elapsed, elapsed2) = ConvertTimeInMillis.calculateTimeInMilliseconds(
                startTime,
                endTime,
                date.text.toString()
            )

            /**
             * if the elapsed2 < 0 that means the from time is less than the current time. In that case
             * we restrict the user to move forword and show some message in alert that the time is not valid
             */
            if (elapsed2 < 0) {
                val builder = GetAleretDialog.getDialog(
                    this,
                    getString(R.string.invalid),
                    getString(R.string.invalid_fromtime)
                )
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                }
                var alertDialog = GetAleretDialog.showDialog(builder)
                ColorOfDialogButton.setColorOfDialogButton(alertDialog)
            }

            /**
             * if MIN_MILLISECONDS <= elapsed that means the meeting duration is more than 10 min
             * if the above condition is not true than we show a message in alert that the meeting duration must be greater than 15 min
             * if MAX_MILLISECONDS >= elapsed that means the meeting duration is less than 4hours
             * if the above condition is not true than we show show a message in alert that the meeting duration must be less than 4hours
             * if above both conditions are true than entered time is correct and user is allowed to go to the next actvity
             */
            else if (minMilliseconds <= elapsed) {
                setDataForApiCallToFetchRoomDetails()
            } else {
                val builder = GetAleretDialog.getDialog(
                    this,
                    getString(R.string.invalid),
                    getString(R.string.time_validation_message)
                )
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                }
                var alertBuilder = GetAleretDialog.showDialog(builder)
                ColorOfDialogButton.setColorOfDialogButton(alertBuilder)

            }
        } catch (e: Exception) {
            Log.i("-------------", e.message)
            Toast.makeText(this@InputDetailsForBooking, getString(R.string.details_invalid), Toast.LENGTH_LONG).show()
        }
    }

    private fun setDataForApiCallToFetchRoomDetails() {
        mInputDetailsForRoom.capacity = roomCapacityEditText.text.toString().toInt()
        mInputDetailsForRoom.buildingId = mBuildingId
        mInputDetailsForRoom.fromTime = dateEditText.text.toString() + " " + fromTimeEditText.text.toString()
        mInputDetailsForRoom.toTime = dateEditText.text.toString() + " " + toTimeEditText.text.toString()
        if (NetworkState.appIsConnectedToInternet(this)) {
            getViewModelForConferenceRoomList(mInputDetailsForRoom)
        } else {
            val i = Intent(this, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE2)
        }
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
     * sign out from application
     */
    private fun signOut() {
        val mGoogleSignInClient = GoogleGSO.getGoogleSignInClient(this)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                startActivity(Intent(applicationContext, SignIn::class.java))
                finish()
            }
    }
    /**
     * get token and userId from local storage
     */
    private fun getTokenFromPreference(): String {
        return getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("Token", "Not Set")!!
    }
}


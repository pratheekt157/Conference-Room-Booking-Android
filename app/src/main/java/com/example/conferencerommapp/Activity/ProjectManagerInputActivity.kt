package com.example.conferencerommapp.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html.fromHtml
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.Model.Building
import com.example.conferencerommapp.Model.GetIntentDataFromActvity
import com.example.conferencerommapp.Model.ManagerConference
import com.example.conferencerommapp.Model.RoomDetails
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.BuildingViewModel
import com.example.conferencerommapp.ViewModel.ManagerBuildingViewModel
import com.example.conferencerommapp.ViewModel.ManagerConferenceRoomViewModel
import com.example.conferencerommapp.utils.*
import com.example.conferenceroomtabletversion.utils.GetPreference
import kotlinx.android.synthetic.main.activity_project_manager_input.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NAME_SHADOWING", "DEPRECATION")
class ProjectManagerInputActivity : AppCompatActivity() {
    @BindView(R.id.fromTime_manager)
    lateinit var fromTimeEditText: EditText
    @BindView(R.id.toTime_manager)
    lateinit var toTimeEditText: EditText
    @BindView(R.id.to_date_manager)
    lateinit var dateToEditText: EditText
    @BindView(R.id.date_manager)
    lateinit var dateFromEditText: EditText
    private lateinit var mManagerBuildingViewModel: ManagerBuildingViewModel
    @BindView(R.id.manager_recycler_view_room_list)
    lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.manager_building_name_spinner)
    lateinit var buildingSpineer: Spinner
    @BindView(R.id.manager_room_capacity)
    lateinit var roomCapacityEditText: EditText
    @BindView(R.id.manager_event_name_text_view)
    lateinit var purposeEditText: EditText
    private lateinit var mManagerConferecneRoomViewModel: ManagerConferenceRoomViewModel
    private lateinit var customAdapter: RoomAdapter
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mIntentDataFromActivity: GetIntentDataFromActvity
    private lateinit var mBuildingsViewModel: BuildingViewModel
    private var listOfDays = ArrayList<String>()
    private var dataList = ArrayList<String>()
    private var fromTimeList = ArrayList<String>()
    private var toTimeList = ArrayList<String>()
    var mRoom = ManagerConference()
    var mSetIntentData = GetIntentDataFromActvity()
    private var mSuggestedRoomApiIsCallled = false
    var mBuildingName = "Select Building"
    var mBuildingId = -1
    var buildingId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_manager_input)
        ButterKnife.bind(this)
        init()
        observerData()
    }

    private fun init() {
        initActionBar()
        initLateInitializervariables()
        addTextChangeListener()
        setPickerToEditTexts()
        if (NetworkState.appIsConnectedToInternet(this)) {
            getViewModelForBuildingList()
        } else {
            val i = Intent(this@ProjectManagerInputActivity, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
    }

    private fun initLateInitializervariables() {
        mManagerBuildingViewModel = ViewModelProviders.of(this).get(ManagerBuildingViewModel::class.java)
        mBuildingsViewModel = ViewModelProviders.of(this).get(BuildingViewModel::class.java)
        mManagerConferecneRoomViewModel = ViewModelProviders.of(this).get(ManagerConferenceRoomViewModel::class.java)
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message), this)
        mIntentDataFromActivity = GetIntentDataFromActvity()
    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title = fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Buildings) + "</font>")
    }

    private fun addTextChangeListener() {
        textChangeListenerOnFromTimeEditText()
        textChangeListenerOnToTimeEditText()
        textChangeListenerOnFromDateEditText()
        textChangeListenerOnToDateEditText()
        textChangeListenerOnRoomCapacity()
        textChangeListenerOnPurposeEditText()

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            getViewModelForBuildingList()
        }
    }

    @OnClick(R.id.manager_search_room)
    fun makeCallForRooms() {
        if (validate()) {
            manager_suggestions.visibility = View.GONE
            applyValidationOnDateAndTime()
        }
    }
    /**
     * get all date for each day selected by user in between from date and To date
     */
    private fun getDateAccordingToDay(
        start: String,
        end: String,
        fromDate: String,
        toDate: String,
        listOfDays: ArrayList<String>
    ) {
        dataList.clear()
        try {
            val simpleDateFormat = SimpleDateFormat(Constants.DATE_FORMAAT_Y_D_M, Locale.US)
            val d1 = simpleDateFormat.parse(fromDate)
            val d2 = simpleDateFormat.parse(toDate)
            val c1 = Calendar.getInstance()
            val c2 = Calendar.getInstance()
            c1.time = d1
            c2.time = d2
            while (c2.after(c1)) {
                if (listOfDays.contains(
                        c1.getDisplayName(
                            Calendar.DAY_OF_WEEK,
                            Calendar.LONG_FORMAT,
                            Locale.US
                        ).toUpperCase()
                    )
                ) {
                    dataList.add(simpleDateFormat.format(c1.time).toString())
                }
                c1.add(Calendar.DATE, 1)
            }
            if (listOfDays.contains(
                    c2.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.LONG_FORMAT,
                        Locale.US
                    ).toUpperCase()
                )
            ) {
                dataList.add(simpleDateFormat.format(c1.time).toString())
            }
            getLists(start, end)
        } catch (e: Exception) {
            Toast.makeText(this@ProjectManagerInputActivity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * this function returns all fromdate list and todate list
     */
    private fun getLists(start: String, end: String) {
        fromTimeList.clear()
        toTimeList.clear()
        for (item in dataList) {
            fromTimeList.add(FormatTimeAccordingToZone.formatDateAsUTC("$item $start"))
            toTimeList.add(FormatTimeAccordingToZone.formatDateAsUTC("$item $end"))
        }
    }



    //observe data from view model
    private fun observerData() {
        mBuildingsViewModel.returnMBuildingSuccess().observe(this, Observer {
            progressDialog.dismiss()
            setBuildingSpinner(it)
        })
        mBuildingsViewModel.returnMBuildingFailure().observe(this, Observer {
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
            }
        })


        //positive response
        mManagerConferecneRoomViewModel.returnSuccess().observe(this, Observer {
            checkForStatusOfRooms(it)
        })

        // Negative response
        mManagerConferecneRoomViewModel.returnFailure().observe(this, Observer {
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
                //finish()
            }
        })

        //positive response for suggested rooms
       mManagerConferecneRoomViewModel.returnSuccessForSuggested().observe(this, Observer {
           manager_horizontal_line_below_search_button.visibility = View.VISIBLE
           if(it.isEmpty()) {
               manager_suggestions.text = "No Rooms Available"
           } else {
               manager_suggestions.text = "No Room Available.Have look on suggestions"
           }
           manager_suggestions.visibility = View.VISIBLE
           setAdapter(it)
       })
        // negative response for suggested rooms
        mManagerConferecneRoomViewModel.returnFailureForSuggestedRooms().observe(this, Observer {
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
                finish()
            }
        })
    }
    private fun checkForStatusOfRooms(mListOfRooms: List<RoomDetails>?) {
        var count = 0
        for (room in mListOfRooms!!) {
            if (room.status == getString(R.string.unavailable)) {
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
    private fun setAdapter(mListOfRooms: List<RoomDetails>) {
        progressDialog.dismiss()
        customAdapter =
            RoomAdapter(mListOfRooms as ArrayList<RoomDetails>, this, object : RoomAdapter.ItemClickListener {
                override fun onItemClick(roomId: Int?, buidingId: Int?, roomName: String?, buildingName: String?) {
                    mSetIntentData.fromTime = fromTimeEditText.text.toString()
                    mSetIntentData.toTime = toTimeEditText.text.toString()
                    mSetIntentData.date = dateFromEditText.text.toString()
                    mSetIntentData.toDate = dateToEditText.text.toString()
                    mSetIntentData.buildingName = mBuildingName
                    mSetIntentData.roomName = roomName
                    mSetIntentData.buildingId = mBuildingId
                    mSetIntentData.roomId = roomId
                    mSetIntentData.fromTimeList.clear()
                    mSetIntentData.toTimeList.clear()
                    mSetIntentData.fromTimeList.addAll(fromTimeList)
                    mSetIntentData.toTimeList.addAll(toTimeList)
                    mSetIntentData.capacity = roomCapacityEditText.text.toString()
                    mSetIntentData.purpose = purposeEditText.text.toString()
                    goToSelectMeetingMembersActivity()
                }
            })
        mRecyclerView.adapter = customAdapter
    }

    private fun goToSelectMeetingMembersActivity() {
        var intent = Intent(this@ProjectManagerInputActivity, ManagerSelectMeetingMembers::class.java)
        intent.putExtra(Constants.EXTRA_INTENT_DATA, mSetIntentData)
        startActivity(intent)
    }

    private fun makeCallToApiForSuggestedRooms() {
        mSuggestedRoomApiIsCallled = true
        mManagerConferecneRoomViewModel.getSuggestedConferenceRoomList(GetPreference.getTokenFromPreference(this), mRoom)
    }

    /**
     * get the object of ViewModel using ViewModelProviders and observers the data from backend
     */
    private fun getViewModelForBuildingList() {
        progressDialog.show()
        // make api call
        mBuildingsViewModel.getBuildingList(GetPreference.getTokenFromPreference(this))
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
            manager_capacity_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            val input = roomCapacityEditText.text.toString().toLong()
            if (input <= 0 || input > Int.MAX_VALUE) {
                manager_capacity_layout.error = getString(R.string.room_capacity_must_be_more_than_0)
                false
            } else {
                manager_capacity_layout.error = null
                true
            }
        }
    }

    /**
     * set date and time pickers to edittext fields
     */
    private fun setPickerToEditTexts() {

        /**
         * set Time picker for the editText fromTimeEditText
         */
        fromTimeEditText.setOnClickListener {
            DateAndTimePicker.getTimePickerDialog(this, fromTimeEditText)
        }
        /**
         * set Time picker for the EditText toTimeEditText
         */
        toTimeEditText.setOnClickListener {
            DateAndTimePicker.getTimePickerDialog(this, toTimeEditText)
        }
        /**
         * set Date picker for the EditText dateEditText
         */
        dateFromEditText.setOnClickListener {
            DateAndTimePicker.getDatePickerDialog(this, dateFromEditText)
        }
        /**
         * set Date picker for the EditText dateToEditText
         */
        dateToEditText.setOnClickListener {
            DateAndTimePicker.getDatePickerDialog(this, dateToEditText)
        }
    }

    /**
     * validate from time for non empty condition
     */
    private fun validateFromTime(): Boolean {
        val input = fromTimeEditText.text.toString().trim()
        return if (input.isEmpty()) {
            manager_from_time_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            manager_from_time_layout.error = null
            true
        }
    }

    /**
     * validate to-time for non empty condition
     */
    private fun validateToTime(): Boolean {
        val input = toTimeEditText.text.toString().trim()
        return if (input.isEmpty()) {
            manager_to_time_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            manager_to_time_layout.error = null
            true
        }
    }

    /**
     * validate to-date for non empty condition
     */
    private fun validateToDate(): Boolean {
        val input = dateFromEditText.text.toString().trim()
        return if (input.isEmpty()) {
            manager_from_date_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            manager_from_date_layout.error = null
            true
        }
    }

    /**
     * validate from-date for non empty condition
     */
    private fun validateFromDate(): Boolean {
        val input = dateToEditText.text.toString().trim()
        return if (input.isEmpty()) {
            manager_to_date_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            manager_to_date_layout.error = null
            true
        }
    }

    /**
     * validate day selector for non empty condition
     */
    private fun validateSelectedDayList(): Boolean {
        if (day_picker.selectedDays.isEmpty()) {
            error_day_selector_text_view.visibility = View.VISIBLE
            return false
        }
        error_day_selector_text_view.visibility = View.INVISIBLE
        return true
    }

    /**
     * validate building spinner
     */
    private fun validateBuildingSpinner(): Boolean {
        return if (mBuildingName == getString(R.string.select_building)) {
            manager_text_view_error_spinner_building.visibility = View.VISIBLE
            false
        } else {
            manager_text_view_error_spinner_building.visibility = View.INVISIBLE
            true
        }
    }


    /**
     * this function ensures that user entered values for all editable fields
     */
    private fun validate(): Boolean {
        if (!validateFromTime() or !validateToTime() or !validateFromDate() or !validateToDate() or !validateSelectedDayList() or !validateBuildingSpinner() or !validateRoomCapacity() or !validatePurpose()) {
            return false
        }
        return true
    }

    /**
     * if MIN_MILIISECONDS <= elapsed that means the meeting duration is more than 15 min
     *  if the above condition is not true than we show a message in alert that the meeting duration must be greater than 15 min
     *  if MAX_MILLISECONDS >= elapsed that means the meeting duration is less than 4hours
     *  if the above condition is not true than we show show a message in alert that the meeting duration must be less than 4hours
     *  if above both conditions are true than entered time is correct and user is allowed to go to the next actvity
     */
    private fun applyValidationOnDateAndTime() {
        val minMilliseconds: Long = Constants.MIN_MEETING_DURATION
        /**
         * Get the start and end time of meeting from the input fields
         */

        val startTime = fromTimeEditText.text.toString()
        val endTime = toTimeEditText.text.toString()

        /**
         * setting a alert dialog instance for the current context
         */
        val builder = android.app.AlertDialog.Builder(this@ProjectManagerInputActivity)
        builder.setTitle("Check...")
        try {

            /**
             *  getting the values for time validation variables from method calculateTimeInMillis
             */
            val (elapsed, elapsed2) = ConvertTimeInMillis.calculateTimeInMilliseconds(
                startTime,
                endTime,
                dateFromEditText.text.toString()
            )
            /**
             * if the elapsed2 < 0 that means the from time is less than the current time. In that case
             * we restrict the user to move forword and show some message in alert that the time is not valid
             */

            if (elapsed2 < 0) {
                val builder =
                    GetAleretDialog.getDialog(this, getString(R.string.invalid), getString(R.string.invalid_fromtime))
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                }
                GetAleretDialog.showDialog(builder)
            } else if (minMilliseconds <= elapsed) {
                if (ConvertTimeInMillis.calculateDateInMilliseconds(
                        dateFromEditText.text.toString(),
                        dateToEditText.text.toString()
                    )
                ) {
                    goToBuildingsActivity()
                } else {
                    Toast.makeText(this, getString(R.string.invalid_fromDate_or_toDate), Toast.LENGTH_SHORT).show()
                }

            } else {
                val builder = GetAleretDialog.getDialog(
                    this,
                    getString(R.string.invalid),
                    getString(R.string.time_validation_message)
                )

                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                }
                var dialog = GetAleretDialog.showDialog(builder)
                ColorOfDialogButton.setColorOfDialogButton(dialog)
            }
        } catch (e: Exception) {
            Toast.makeText(this@ProjectManagerInputActivity, "Details are Invalid!!!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * set data to the object which is used to send data from this activity to another activity and pass the intent
     */
    private fun goToBuildingsActivity() {
        mIntentDataFromActivity.listOfDays.clear()
        getListOfSelectedDays()
        getDateAccordingToDay(
            fromTimeEditText.text.toString(),
            toTimeEditText.text.toString(),
            dateFromEditText.text.toString(),
            dateToEditText.text.toString(),
            listOfDays
        )
        mRoom.fromTime.clear()
        mRoom.toTime.clear()
        mRoom.fromTime.addAll(fromTimeList)
        mRoom.toTime.addAll(toTimeList)
        mRoom.capacity = roomCapacityEditText.text.toString().toInt()
        mRoom.buildingId = mBuildingId
        if (dataList.isEmpty()) {
            Toast.makeText(this, "No dates founds for selected days!", Toast.LENGTH_SHORT).show()
        } else {
            getConferenceRoomViewModel()
        }
    }

    private fun getConferenceRoomViewModel() {
        if (NetworkState.appIsConnectedToInternet(this)) {
            progressDialog.show()
            mManagerConferecneRoomViewModel.getConferenceRoomList(mRoom, GetPreference.getTokenFromPreference(this))
        } else {

        }

    }

    /**
     * get all the selected days and add all days to another list listOfDays
     */
    private fun getListOfSelectedDays() {
        listOfDays.clear()
        for (day in day_picker.selectedDays) {
            listOfDays.add("${day}")
        }
    }
    /**
     * add text change listener for the start time edit text
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
     * add text change listener for the end time edit text
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
     * add text change listener for the start edit text
     */
    private fun textChangeListenerOnFromDateEditText() {
        dateFromEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateToDate()

            }
        })
    }

    /**
     * add text change listener for the end date edit text
     */
    private fun textChangeListenerOnToDateEditText() {
        dateToEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFromDate()
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
     * validate all input fields
     */
    private fun validatePurpose(): Boolean {
        return if (purposeEditText.text.toString().trim().isEmpty()) {
            manager_purpose_layout.error = getString(R.string.field_cant_be_empty)
            false
        }else {
            manager_purpose_layout.error = null
            true
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
    private fun setBuildingSpinner(mBuildingList: List<Building>) {
        var buildingNameList = mutableListOf<String>()
        var buildingIdList = mutableListOf<Int>()

        buildingNameList.add(getString(R.string.select_building))
        buildingIdList.add(-1)
        for (mBuilding in mBuildingList) {
            buildingNameList.add(mBuilding.buildingName!!)
            buildingIdList.add(mBuilding.buildingId!!.toInt())
        }
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_icon, R.id.spinner_text, buildingNameList)
        buildingSpineer.adapter = adapter
        buildingSpineer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                mBuildingName = buildingNameList[position]
                mBuildingId = buildingIdList[position]
                manager_text_view_error_spinner_building.visibility = View.INVISIBLE
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                mBuildingName = getString(R.string.select_building)
            }
        }
    }
}

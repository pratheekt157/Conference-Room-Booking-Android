package com.example.conferencerommapp.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.Model.EmployeeList
import com.example.conferencerommapp.Model.GetIntentDataFromActvity
import com.example.conferencerommapp.Model.ManagerBooking
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.ManagerBookingViewModel
import com.example.conferencerommapp.ViewModel.SelectMemberViewModel
import com.example.conferencerommapp.utils.*
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.chip.Chip
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_select_meeting_members.*
import java.util.regex.Pattern

class ManagerSelectMeetingMembers : AppCompatActivity() {
    val employeeList = ArrayList<EmployeeList>()
    private val selectedName = ArrayList<String>()
    private val selectedEmail = ArrayList<String>()
    private lateinit var customAdapter: SelectMembers
    @BindView(R.id.select_meeting_members_progress_bar)
    lateinit var mProgressBar: ProgressBar
    @BindView(R.id.search_edit_text)
    lateinit var searchEditText: EditText
    @BindView(R.id.add_email)
    lateinit var addEmailButton: Button
    private lateinit var mSelectMemberViewModel: SelectMemberViewModel
    lateinit var progressDialog: ProgressDialog
    private lateinit var mManagerBookingViewModel: ManagerBookingViewModel
    private lateinit var acct: GoogleSignInAccount
    private var mManagerBooking = ManagerBooking()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_meeting_members)
        ButterKnife.bind(this)
        init()
        observeData()
        setClickListenerOnEditText()
        //clear search edit text data
        searchEditText.onRightDrawableClicked {
            it.text.clear()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            getViewModel()
        }
    }

    @OnClick(R.id.add_email)
    fun checkSearchEditTextContent() {
        if(validateEmailFormat()) {
            var email = searchEditText.text.toString().trim()
            if(email == acct.email) {
                Toast.makeText(this, getString(R.string.already_part_of_meeting), Toast.LENGTH_SHORT).show()
                return
            }
            addChip(email, email)
        } else {
            Toasty.info(this, getString(R.string.wrong_email), Toasty.LENGTH_SHORT, true).show()
        }
    }

    /**
     * initialize all lateinit fields
     */
    fun init() {
        initActionBar()
        initLateInitializerVariables()
        if (NetworkState.appIsConnectedToInternet(this)) {
            getViewModel()
        } else {
            val i = Intent(this, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title =
            Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.select_participipants) + "</font>")


    }

    private fun initLateInitializerVariables() {
        acct = GoogleSignIn.getLastSignedInAccount(applicationContext)!!
        mManagerBookingViewModel = ViewModelProviders.of(this).get(ManagerBookingViewModel::class.java)
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message), this)
        mSelectMemberViewModel = ViewModelProviders.of(this).get(SelectMemberViewModel::class.java)
    }

    private fun getViewModel() {
        mProgressBar.visibility = View.VISIBLE
        mSelectMemberViewModel.getEmployeeList(GetPreference.getTokenFromPreference(this))
    }

    /**
     * observer data from viewmodel
     */
    private fun observeData() {
        mSelectMemberViewModel.returnSuccessForEmployeeList().observe(this, Observer {
            mProgressBar.visibility = View.GONE
            if (it.isEmpty()) {
                Toasty.info(this, "Empty EmployeeList", Toast.LENGTH_SHORT, true).show()
                finish()
            } else {
                employeeList.clear()
                employeeList.addAll(it)
                customAdapter = SelectMembers(it, object : SelectMembers.ItemClickListener {
                    override fun onBtnClick(name: String?, email: String?) {
                        addChip(name!!, email!!)
                    }

                })
                select_member_recycler_view.adapter = customAdapter
            }
        })
        mSelectMemberViewModel.returnFailureForEmployeeList().observe(this, Observer {
            mProgressBar.visibility = View.GONE
            if (it == getString(R.string.invalid_token)) {
                ShowDialogForSessionExpired.showAlert(this, ManagerSelectMeetingMembers())
            } else {
                ShowToast.show(this, it as Int)
                finish()
            }
        })

        // observer for add Booking
        mManagerBookingViewModel.returnSuccessForBooking().observe(this, Observer {
            progressDialog.dismiss()
            goToBookingDashboard()
        })
        mManagerBookingViewModel.returnFailureForBooking().observe(this, Observer {
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                ShowDialogForSessionExpired.showAlert(this, ManagerSelectMeetingMembers())
            } else {
                ShowToast.show(this, it as Int)
                finish()
            }
        })
    }

    /**
     * set values to the different properties of object which is required for api call
     */
    private fun addDataToObject() {
        var mGetIntentDataFromActvity = getIntentData()
        mManagerBooking.email = acct.email
        mManagerBooking.roomId = mGetIntentDataFromActvity.roomId!!.toInt()
        mManagerBooking.buildingId = mGetIntentDataFromActvity.buildingId!!.toInt()
        mManagerBooking.fromTime = mGetIntentDataFromActvity.fromTimeList
        mManagerBooking.toTime = mGetIntentDataFromActvity.toTimeList
        mManagerBooking.purpose = mGetIntentDataFromActvity.purpose
        mManagerBooking.roomName = mGetIntentDataFromActvity.roomName
    }

    /**
     * go to UserBookingDashboardActivity
     */
    private fun goToBookingDashboard() {
        Toasty.success(this, getString(R.string.booked_successfully), Toast.LENGTH_SHORT, true).show()
        startActivity(Intent(this, UserBookingsDashboardActivity::class.java))
        finish()
    }

    @OnClick(R.id.next_activity)
    fun onClick() {
        var emailString = ""
        val size = selectedName.size
        selectedEmail.indices.forEach { index ->
            emailString += selectedEmail[index]
            if (index != (size - 1)) {
                emailString += ","
            }
        }
        mManagerBooking.cCMail = emailString
        val dialog = GetAleretDialog.getDialog(this, getString(R.string.confirm), getString(R.string.book_confirmation_message))
        dialog.setPositiveButton(getString(R.string.book)) { _, _ ->
            if (NetworkState.appIsConnectedToInternet(this)) {
                addDataToObject()
                addBooking()
            } else {
                val i = Intent(this@ManagerSelectMeetingMembers, NoInternetConnectionActivity::class.java)
                startActivityForResult(i, Constants.RES_CODE2)
            }
        }
        dialog.setNegativeButton(R.string.no) { _, _ ->
            // do nothing
        }
        val builder = GetAleretDialog.showDialog(dialog)
        ColorOfDialogButton.setColorOfDialogButton(builder)
    }

    /**
     * function sets a observer which will observe the data from backend and add the booking details to the database
     */
    private fun addBooking() {
        progressDialog.show()
        mManagerBookingViewModel.addBookingDetails(mManagerBooking, GetPreference.getTokenFromPreference(this))
    }

    fun addChip(name: String, email: String) {
        if (!selectedEmail.contains(email)) {
            val chip = Chip(this)
            chip.text = name
            chip.isCloseIconVisible = true
            chip_group.addView(chip)
            chip.setOnCloseIconClickListener {
                selectedName.remove(name)
                selectedEmail.remove(email)
                chip_group.removeView(chip)
            }
            selectedName.add(name)
            selectedEmail.add(email)
        } else {
            Toast.makeText(this, getString(R.string.already_selected), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * get data from Intent
     */
    private fun getIntentData(): GetIntentDataFromActvity {
        return intent.extras!!.get(Constants.EXTRA_INTENT_DATA) as GetIntentDataFromActvity
    }

    /**
     * clear text in search bar whenever clear drawable clicked
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText && event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
            hasConsumed
        }
    }

    /**
     * take input from edit text and set addTextChangedListener
     */
    private fun setClickListenerOnEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                /**
                 * Nothing Here
                 */
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(charSequence.isEmpty()) {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_search,0)
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_clear,0)
                }
            }

            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }


    /**
     * filter matched data from employee list and set updated list to adapter
     */
    fun filter(text: String) {
        val filterName = java.util.ArrayList<EmployeeList>()
        for (s in employeeList) {
            if (s.name!!.toLowerCase().contains(text.toLowerCase())) {
                filterName.add(s)
            }
        }
        customAdapter.filterList(filterName)
        // no items present in recyclerview than give option for add other emails
        if(customAdapter.itemCount == 0) {
            addEmailButton.visibility = View.VISIBLE
        }else {
            addEmailButton.visibility = View.GONE
        }
    }



    private fun validateEmailFormat(): Boolean {
        var email = searchEditText.text.toString().trim()
        val pat = Pattern.compile(Constants.MATCHER)
        return pat.matcher(email).matches()
    }

}

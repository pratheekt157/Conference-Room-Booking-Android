package com.example.conferencerommapp.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.Model.*
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.BookingDashboardViewModel
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_cancelled_booking.*
import kotlinx.android.synthetic.main.fragment_upcoming_booking.*
import java.text.SimpleDateFormat

class UpcomingBookingFragment : Fragment() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var finalList = ArrayList<Dashboard>()
    private lateinit var mBookingDashBoardViewModel: BookingDashboardViewModel
    private lateinit var acct: GoogleSignInAccount
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mBookingListAdapter: UpcomingBookingAdapter
    private var bookingId = 0
    private var makeApiCallOnResume = false
    var pagination: Int = 1
    var hasMoreItem: Boolean = false
    var mBookingDashboardInput = BookingDashboardInput()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_upcoming_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeData()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            getViewModel()
        }
        if (requestCode == Constants.RES_CODE2 && resultCode == Activity.RESULT_OK) {
            cancelBooking(bookingId)
        }
    }

    /**
     * Initialize all late init fields
     */
    @SuppressLint("ResourceAsColor")
    fun init() {
        initRecyclerView()
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message), activity!!)
        acct = GoogleSignIn.getLastSignedInAccount(activity)!!
        mBookingDashBoardViewModel = ViewModelProviders.of(this).get(BookingDashboardViewModel::class.java)
        booking_refresh_layout.setColorSchemeColors(R.color.colorPrimary)
        refreshOnPullDown()
        mBookingDashboardInput.pageSize = 5
        mBookingDashboardInput.status = Constants.BOOKING_DASHBOARD_TYPE_UPCOMING
        mBookingDashboardInput.pageNumber = pagination
        mBookingDashboardInput.email = acct.email.toString()
        if (NetworkState.appIsConnectedToInternet(activity!!)) {
            getViewModel()
        } else {
            val i = Intent(activity!!, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
    }

    private fun getViewModel() {
        progressDialog.show()
        mBookingDashBoardViewModel.getBookingList(
            GetPreference.getTokenFromPreference(activity!!),
            mBookingDashboardInput
        )
    }

    private fun initRecyclerView() {
        mBookingListAdapter = UpcomingBookingAdapter(
            finalList,
            activity!!,
            object : UpcomingBookingAdapter.CancelBtnClickListener {
                override fun onCLick(position: Int) {
                    showConfirmDialogForCancelMeeting(position)
                }
            },
            object : UpcomingBookingAdapter.ShowMembersListener {
                override fun showMembers(mEmployeeList: List<String>, position: Int) {
                    showMeetingMembers(mEmployeeList, position)
                }

            },
            object : UpcomingBookingAdapter.EditBookingListener {
                override fun editBooking(mGetIntentDataFromActvity: GetIntentDataFromActvity) {
                    intentToUpdateBookingActivity(mGetIntentDataFromActvity)
                }
            }
        )
        dashBord_recyclerView1.adapter = mBookingListAdapter
        dashBord_recyclerView1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    // todo check if the there are more items existing in database
                    if (hasMoreItem) {
                        pagination++
                        mBookingDashboardInput.pageNumber = pagination
                        upcoming_booking_progress_bar.visibility = View.VISIBLE
                        mBookingDashBoardViewModel.getBookingList(
                            GetPreference.getTokenFromPreference(activity!!),
                            mBookingDashboardInput
                        )
                    }
                }
            }
        })
    }

    /**
     * add refresh listener on pull down
     */
    private fun refreshOnPullDown() {
        booking_refresh_layout.setOnRefreshListener {
            finalList.clear()
            pagination = 1
            mBookingDashboardInput.pageNumber = pagination
            if (NetworkState.appIsConnectedToInternet(activity!!)) {
                mBookingDashBoardViewModel.getBookingList(
                    GetPreference.getTokenFromPreference(activity!!),
                    mBookingDashboardInput
                )
            } else {
                val i = Intent(activity!!, NoInternetConnectionActivity::class.java)
                startActivityForResult(i, Constants.RES_CODE)
            }
        }
    }

    /**
     * all observer for LiveData
     */
    private fun observeData() {

        /**
         * observing data for booking list
         */
        mBookingDashBoardViewModel.returnSuccess().observe(this, Observer {
            upcoming_booking_progress_bar.visibility = View.GONE
            booking_refresh_layout.isRefreshing = false
            progressDialog.dismiss()
            hasMoreItem = it.paginationMetaData!!.nextPage!!
            setFilteredDataToAdapter(it.dashboard!!)
        })
        mBookingDashBoardViewModel.returnFailure().observe(this, Observer {
            upcoming_booking_progress_bar.visibility = View.GONE
            booking_refresh_layout.isRefreshing = false
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else if (it == Constants.NO_CONTENT_FOUND && finalList.size == 0) {
                upcoming_empty_view.visibility = View.VISIBLE
                r1_dashboard.setBackgroundColor(Color.parseColor("#F7F7F7"))
            } else {
                ShowToast.show(activity!!, it as Int)
            }
        })

        /**
         * observing data for cancel booking
         */
        mBookingDashBoardViewModel.returnBookingCancelled().observe(this, Observer {
            Toasty.success(activity!!, getString(R.string.cancelled_successful), Toast.LENGTH_SHORT, true).show()
            // make api call to get the updated list of booking after cancellation
            pagination = 1
            mBookingDashboardInput.pageNumber = pagination
            finalList.clear()
            dashBord_recyclerView1.adapter?.notifyDataSetChanged()
            mBookingDashBoardViewModel.getBookingList(
                GetPreference.getTokenFromPreference(activity!!),
                mBookingDashboardInput
            )
        })

        mBookingDashBoardViewModel.returnCancelFailed().observe(this, Observer {
            progressDialog.dismiss()
            if (it == getString(R.string.invalid_token)) {
                showAlert()
            } else {
                ShowToast.show(activity!!, it as Int)
            }
        })

    }


    /**
     * Display the list of employee names in the alert dialog
     */
    fun showMeetingMembers(mEmployeeList: List<String>, position: Int) {
        val arrayListOfNames = ArrayList<String>()
        arrayListOfNames.add(finalList[position].organizer + "(Organizer)")
        for (item in mEmployeeList) {
            arrayListOfNames.add(item)
        }
        val listItems = arrayOfNulls<String>(arrayListOfNames.size)
        arrayListOfNames.toArray(listItems)
        val builder = AlertDialog.Builder(activity!!)
        builder.setItems(
            listItems
        ) { _, _ -> }
        val mDialog = builder.create()
        mDialog.show()
    }


    /**
     * this function will call a function which will filter the data after that set the filtered data to adapter
     */
    private fun setFilteredDataToAdapter(dashboardItemList: List<Dashboard>) {
        finalList.addAll(dashboardItemList)
        dashBord_recyclerView1.adapter?.notifyDataSetChanged()
    }

    /**
     * function will send intent to the UpdateActivity with data which is required for updation
     */
    fun intentToUpdateBookingActivity(mGetIntentDataFromActivity: GetIntentDataFromActvity) {
        makeApiCallOnResume = true
        val updateActivityIntent = Intent(activity!!, UpdateBookingActivity::class.java)
        updateActivityIntent.putExtra(Constants.EXTRA_INTENT_DATA, mGetIntentDataFromActivity)
        startActivity(updateActivityIntent)
    }


    /**
     * show a dialog to confirm cancel of booking
     * if ok button is pressed than cancelBooking function is called
     */
    fun showConfirmDialogForCancelMeeting(position: Int) {
        val mBuilder =
            GetAleretDialog.getDialog(
                activity!!,
                getString(R.string.cancel),
                getString(R.string.sure_cancel_meeting)
            )
        mBuilder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            /**
             * object which is required for the API call
             */
            bookingId = finalList[position].bookingId!!
            if (NetworkState.appIsConnectedToInternet(activity!!)) {
                cancelBooking(finalList[position].bookingId!!)
            } else {
                val i = Intent(activity!!, NoInternetConnectionActivity::class.java)
                startActivityForResult(i, Constants.RES_CODE2)
            }
        }
        mBuilder.setNegativeButton(getString(R.string.no)) { _, _ ->
        }
        val dialog = GetAleretDialog.showDialog(mBuilder)
        ColorOfDialogButton.setColorOfDialogButton(dialog)

    }

    /**
     * A function for cancel a booking
     */
    private fun cancelBooking(mBookingId: Int) {
        progressDialog.show()
        mBookingDashBoardViewModel.cancelBooking(GetPreference.getTokenFromPreference(activity!!), mBookingId)
    }


    /**
     * function will sign out the current user and send control to SignInActivity
     */
    private fun signOut() {
        mGoogleSignInClient = GoogleGSO.getGoogleSignInClient(activity!!)
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(activity!!) {
                Toast.makeText(activity, getString(R.string.sign_out), Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, SignIn::class.java))
                activity!!.finish()
            }
    }

    override fun onResume() {
        super.onResume()
        if (makeApiCallOnResume) {
            finalList.clear()
            pagination = 1
            mBookingDashboardInput.pageNumber = pagination
            getViewModel()
            makeApiCallOnResume = false
        }
    }


    /**
     * show dialog for session expired
     */
    private fun showAlert() {
        val dialog = GetAleretDialog.getDialog(
            activity!!, getString(R.string.session_expired), "Your session is expired!\n" +
                    getString(R.string.session_expired_messgae)
        )
        dialog.setPositiveButton(R.string.ok) { _, _ ->
            signOut()
        }
        val builder = GetAleretDialog.showDialog(dialog)
        ColorOfDialogButton.setColorOfDialogButton(builder)
    }


}


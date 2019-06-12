package com.example.conferencerommapp.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.Model.BookingDashboardInput
import com.example.conferencerommapp.Model.Dashboard
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.BookingDashboardViewModel
import com.example.conferencerommapp.utils.*
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.fragment_previous_booking.*

class PreviousBookingFragment : Fragment() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var finalList = ArrayList<Dashboard>()
    private lateinit var mBookingDashBoardViewModel: BookingDashboardViewModel
    private lateinit var acct: GoogleSignInAccount
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mBookingListAdapter: PreviousBookingAdapter
    var mBookingDashboardInput = BookingDashboardInput()
    var pagination: Int = 1
    var hasMoreItem: Boolean = false
    var isApiCalled: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_previous_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeData()
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
        previous__booking_refresh_layout.setColorSchemeColors(R.color.colorPrimary)
        mBookingDashboardInput.pageSize = 5
        mBookingDashboardInput.status = Constants.BOOKING_DASHBOARD_TYPE_PREVIOUS
        mBookingDashboardInput.pageNumber = pagination
        mBookingDashboardInput.email = acct.email.toString()
        if (NetworkState.appIsConnectedToInternet(activity!!)) {
            getViewModel()
        } else {
            val i = Intent(activity!!, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
        refreshOnPullDown()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            getViewModel()
        }
    }

    private fun getViewModel() {
        progressDialog.show()
        mBookingDashBoardViewModel.getBookingList(GetPreference.getTokenFromPreference(activity!!), mBookingDashboardInput)
    }

    private fun initRecyclerView() {
        mBookingListAdapter = PreviousBookingAdapter(
            finalList,
            activity!!,
            object : PreviousBookingAdapter.ShowMembersListener {
                override fun showMembers(mEmployeeList: List<String>, position: Int) {
                    showMeetingMembers(mEmployeeList, position)
                }

            }
        )
        previous__recyclerView.adapter = mBookingListAdapter
        previous__recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    // todo check if the there are more items existing in database
                    if (!isApiCalled && hasMoreItem) {
                        isApiCalled = true
                        pagination++
                        previous_progress_bar.visibility = View.VISIBLE
                        mBookingDashboardInput.pageNumber = pagination
                        mBookingDashBoardViewModel.getBookingList(GetPreference.getTokenFromPreference(activity!!), mBookingDashboardInput)
                    }
                }
            }
        })
    }

    /**
     * add refresh listener on pull down
     */
    private fun refreshOnPullDown() {
        previous__booking_refresh_layout.setOnRefreshListener {
            finalList.clear()
            pagination = 1
            mBookingDashboardInput.pageNumber = pagination
            mBookingDashBoardViewModel.getBookingList(GetPreference.getTokenFromPreference(activity!!), mBookingDashboardInput)
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
            previous_progress_bar.visibility = View.GONE
            previous__booking_refresh_layout.isRefreshing = false
            progressDialog.dismiss()
            previous__empty_view.visibility = View.GONE
            hasMoreItem = it.paginationMetaData!!.nextPage!!
            setFilteredDataToAdapter(it.dashboard!!)
        })
        mBookingDashBoardViewModel.returnFailure().observe(this, Observer {
            previous_progress_bar.visibility = View.GONE
            previous__booking_refresh_layout.isRefreshing = false
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                ShowDialogForSessionExpired.showAlert(activity!!, UserBookingsDashboardActivity())
            } else if (it == Constants.NO_CONTENT_FOUND && finalList.size == 0) {
                previous__empty_view.visibility = View.VISIBLE
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
        arrayListOfNames.add(finalList[position].organizer!! + "(organizer)")
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
        previous__recyclerView.adapter?.notifyDataSetChanged()
    }
}


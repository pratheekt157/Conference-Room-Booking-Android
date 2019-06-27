package com.example.conferencerommapp.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html.fromHtml
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.Blocked
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.R
import com.example.conferencerommapp.R.color.colorPrimary
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.BlockedDashboardViewModel
import com.example.conferencerommapp.utils.*
import com.example.conferenceroomtabletversion.utils.GetPreference
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_blocked_dashboard.*

@SuppressLint("Registered")
@Suppress("DEPRECATION")
class BlockedDashboard : AppCompatActivity() {

    /**
     * Declaring Global variables and butterknife
     */
    @BindView(R.id.block_recyclerView)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.block_dashboard_refresh_layout)
    lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var blockedAdapter: BlockedDashboardNew
    private lateinit var mBlockedDashboardViewModel: BlockedDashboardViewModel
    private lateinit var progressDialog: ProgressDialog
    private var mBlockRoomList = ArrayList<Blocked>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_dashboard)
        ButterKnife.bind(this)
        init()
        observeData()

    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title = fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Blocked_Rooms) + "</font>")
    }

    private fun initLateInitializerVariables() {
        mBlockedDashboardViewModel = ViewModelProviders.of(this).get(BlockedDashboardViewModel::class.java)
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message), this)
    }

    /**
     * Initialize late init fields
     */
    @SuppressLint("ResourceAsColor")
    fun init() {
        initActionBar()
        initLateInitializerVariables()
        refreshLayout.setColorSchemeColors(colorPrimary)
        if (NetworkState.appIsConnectedToInternet(this)) {
            loadBlocking()
        } else {
            val i = Intent(this, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
        refreshOnPull()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            loadBlocking()
        }
        if (requestCode == Constants.RES_CODE2 && resultCode == Activity.RESULT_OK) {
            //unblockRoom(mRoom)
        }
    }

    /**
     * refresh on pull
     */
    private fun refreshOnPull() {
        refreshLayout.setOnRefreshListener {
            mBlockedDashboardViewModel.getBlockedList(GetPreference.getTokenFromPreference(this))
        }
    }

    /**
     * observing data for BlockDashboardList
     */
    private fun observeData() {
        /**
         * observing data for BlockDashboardList
         */
        mBlockedDashboardViewModel.returnBlockedRoomList().observe(this, Observer {
            refreshLayout.isRefreshing = false
            progressDialog.dismiss()
            empty_view_blocked.visibility = View.GONE
            r2_block_dashboard.setBackgroundColor(Color.parseColor("#F7F7F7"))
            mBlockRoomList.clear()
            mBlockRoomList.addAll(it)
            setAdapter()

        })
        mBlockedDashboardViewModel.returnFailureCodeFromBlockedApi().observe(this, Observer {
            refreshLayout.isRefreshing = false
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                ShowDialogForSessionExpired.showAlert(this, BlockedDashboard())
            } else if (it == Constants.NO_CONTENT_FOUND) {
                empty_view_blocked.visibility = View.VISIBLE
                r2_block_dashboard.setBackgroundColor(Color.parseColor("#FFFFFF"))
                mBlockRoomList.clear()
                setAdapter()
            } else {
                ShowToast.show(this, it as Int)
            }
        })
        /**
         * observing data for Unblocking
         */
        mBlockedDashboardViewModel.returnSuccessCodeForUnBlockRoom().observe(this, Observer {
            progressDialog.dismiss()
            Toasty.success(this,getString(R.string.room_unblocked), Toast.LENGTH_SHORT, true).show()
            mBlockedDashboardViewModel.getBlockedList(GetPreference.getTokenFromPreference(this))
        })
        mBlockedDashboardViewModel.returnFailureCodeForUnBlockRoom().observe(this, Observer {
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                ShowDialogForSessionExpired.showAlert(this, BlockedDashboard())
            } else {
                ShowToast.show(this, it as Int)
            }
        })
    }


    fun setAdapter() {
        blockedAdapter = BlockedDashboardNew(
            mBlockRoomList,
            this,
            object : BlockedDashboardNew.UnblockRoomListener {
                override fun onClickOfUnblock(mBookingId: Int) {
                    if (NetworkState.appIsConnectedToInternet(this@BlockedDashboard)) {
                        confirmAlertDialogToUnblockRoom(mBookingId)
                    } else {
                        val i = Intent(this@BlockedDashboard, NoInternetConnectionActivity::class.java)
                        startActivityForResult(i, Constants.RES_CODE2)
                    }
                }
            })
        recyclerView.adapter = blockedAdapter
    }

    private fun confirmAlertDialogToUnblockRoom(mBookingId: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.confirm))
        builder.setMessage(getString(R.string.unblock_room_confirmation_message))
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            unblockRoom(mBookingId)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        ColorOfDialogButton.setColorOfDialogButton(dialog)
    }


    @OnClick(R.id.maintenance)
    fun blockConferenceActivity() {
        val maintenanceIntent = Intent(applicationContext, BlockConferenceRoomActivity::class.java)
        startActivity(maintenanceIntent)
    }

    override fun onRestart() {
        super.onRestart()
        if (NetworkState.appIsConnectedToInternet(this)) {
            loadBlocking()
        } else {
            val i = Intent(this@BlockedDashboard, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
    }

    /**
     * Redirects to the UserBookingDashBoardActivity
     */
    override fun onBackPressed() {
        startActivity(Intent(this, UserBookingsDashboardActivity::class.java))
        finish()
    }

    /**
     * function calls the ViewModel of blockedList
     */
    private fun loadBlocking() {
        progressDialog.show()
        mBlockedDashboardViewModel.getBlockedList(GetPreference.getTokenFromPreference(this))
    }

    /**
     * function calls the ViewModel of Unblock
     */
    fun unblockRoom(mBookingId: Int) {
        progressDialog.show()
        mBlockedDashboardViewModel.unBlockRoom(GetPreference.getTokenFromPreference(this), mBookingId)
    }
}

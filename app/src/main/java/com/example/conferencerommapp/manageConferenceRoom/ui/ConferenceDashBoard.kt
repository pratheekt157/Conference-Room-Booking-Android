package com.example.conferencerommapp.ConferenceRoomDashboard.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html.fromHtml
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.BaseApplication
import com.example.conferencerommapp.Helper.ConferenceRecyclerAdapter
import com.example.conferencerommapp.Helper.NetworkState
import com.example.conferencerommapp.R
import com.example.conferencerommapp.Repository.ManageConferenceRoomRepository
import com.example.conferencerommapp.ViewModel.ManageConferenceRoomViewModel
import com.example.conferencerommapp.utils.*
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.example.myapplication.Models.ConferenceList
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_conference_dash_board.*
import javax.inject.Inject


@Suppress("DEPRECATION")
class ConferenceDashBoard : AppCompatActivity() {

    @Inject
    lateinit var mManageRoomRepo: ManageConferenceRoomRepository

    @BindView(R.id.conference_list)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.conference_room_dashboard_progress_bar)
    lateinit var mProgressBar: ProgressBar
    var buildingId: Int = 0
    private lateinit var mManageConferenceRoomViewModel: ManageConferenceRoomViewModel
    private lateinit var conferenceRoomAdapter: ConferenceRecyclerAdapter
    private var mConferenceList = ArrayList<ConferenceList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conference_dash_board)
        ButterKnife.bind(this)
        init()
        observeData()
    }

    fun init() {
        initActionBar()
        initComponentForManageRoomRepo()
        initLateInitalizerVariables()
        initManageRoomRepo()
        if (NetworkState.appIsConnectedToInternet(this)) {
            getConference(buildingId)
        } else {
            val i = Intent(this, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }
    }

    private fun initComponentForManageRoomRepo() {
        (application as BaseApplication).getmAppComponent()?.inject(this)
    }

    private fun initManageRoomRepo() {
        mManageConferenceRoomViewModel.setManageRoomRepo(mManageRoomRepo)
    }

    private fun initLateInitalizerVariables() {
        buildingId = getIntentData()
        mManageConferenceRoomViewModel = ViewModelProviders.of(this).get(ManageConferenceRoomViewModel::class.java)
    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title = fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Conference_Rooms) + "</font>")
    }

    override fun onRestart() {
        super.onRestart()
        when {
            NetworkState.appIsConnectedToInternet(this) -> getConference(buildingId)
            else -> {
                val i = Intent(this, NoInternetConnectionActivity::class.java)
                startActivityForResult(i, Constants.RES_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            getConference(buildingId)
        }
    }

    private fun observeData() {
        mManageConferenceRoomViewModel.returnConferenceRoomList().observe(this, Observer {
            mProgressBar.visibility = View.GONE
            empty_view_blocked1.visibility = View.GONE
            mConferenceList.clear()
            when {
                it.isNotEmpty() -> mConferenceList.addAll(it)
                else -> {

                    empty_view_blocked1.visibility = View.VISIBLE
                    empty_view_blocked1.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }
            setAdapter()
        })
        mManageConferenceRoomViewModel.returnFailureForConferenceRoom().observe(this, Observer {
            mProgressBar.visibility = View.GONE
            when (it) {
                Constants.INVALID_TOKEN -> ShowDialogForSessionExpired.signOut(this, ConferenceDashBoard())
                else -> {
                    ShowToast.show(this, it as Int)
                    finish()
                }
            }
        })

        mManageConferenceRoomViewModel.returnSuccessForDeleteRoom().observe(this, Observer {
            mProgressBar.visibility = View.GONE
            Toasty.success(this, getString(R.string.successfull_deletion)).show()
            getConference(buildingId)
        })

        mManageConferenceRoomViewModel.returnFailureForDeleteRoom().observe(this, Observer {
            mProgressBar.visibility = View.GONE
            if (it == getString(R.string.invalid_token)) {
                ShowDialogForSessionExpired.showAlert(this, UserBookingsDashboardActivity())
            } else {
                ShowToast.show(this, it as Int)
            }
        })
    }

    private fun setAdapter() {
        conferenceRoomAdapter =
            ConferenceRecyclerAdapter(mConferenceList, object : ConferenceRecyclerAdapter.EditRoomDetails {
                override fun editRoom(position: Int) {
                    val intent = Intent(this@ConferenceDashBoard, AddingConference::class.java)
                    val editRoomDetails = EditRoomDetails()
                    editRoomDetails.mRoomDetail = mConferenceList[position]
                    intent.putExtra(Constants.FLAG, true)
                    intent.putExtra(Constants.EXTRA_INTENT_DATA, editRoomDetails)
                    startActivity(intent)
                }

            },
                object : ConferenceRecyclerAdapter.DeleteClickListner {
                    override fun deleteRoom(position: Int) {
                        showAlertDialogForDelete(mConferenceList[position].roomId)
                    }

                })
        recyclerView.adapter = conferenceRoomAdapter
    }

    private fun showAlertDialogForDelete(roomId: Int?) {
        val dialog = GetAleretDialog.getDialog(this, "Delete", "Are you sure you wnat to delete the Room")
        dialog.setPositiveButton(R.string.ok) { _, _ ->
            mManageConferenceRoomViewModel.deleteConferenceRoom(GetPreference.getTokenFromPreference(this), roomId!!)
        }
        dialog.setNegativeButton(R.string.cancel) { _, _ ->

        }
        val builder = GetAleretDialog.showDialog(dialog)
        ColorOfDialogButton.setColorOfDialogButton(builder)
    }

    /**
     * onClick on this button goes to AddingConference Activity
     */
    @OnClick(R.id.add_conferenece)
    fun addConfereeRoomFloatingActionButton() {
        goToNextActivity(buildingId)

    }

    /**
     * get the buildingId from the BuildingDashboard Activity
     */
    private fun getIntentData(): Int {
        val bundle: Bundle? = intent.extras!!
        return bundle!!.get(Constants.EXTRA_BUILDING_ID)!!.toString().toInt()
    }

    /**
     * Passing Intent and shared preference
     */
    private fun goToNextActivity(buildingId: Int) {
        val pref = getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(Constants.EXTRA_BUILDING_ID, buildingId)
        editor.apply()

        val intent = Intent(this, AddingConference::class.java)
        intent.putExtra(Constants.FLAG, false)
        intent.putExtra(Constants.EXTRA_BUILDING_ID, buildingId)
        startActivity(intent)
    }

    /**
     * function calls the ViewModel of ConferecenceRoom and observe data from the database
     */
    private fun getConference(buildingId: Int) {
        /**
         * getting Progress Dialog
         */
        mProgressBar.visibility = View.VISIBLE
        mManageConferenceRoomViewModel.getConferenceRoomList(buildingId, GetPreference.getTokenFromPreference(this))
    }
}


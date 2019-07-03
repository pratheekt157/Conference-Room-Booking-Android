@file:Suppress("DEPRECATION")

package com.example.conferencerommapp.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.Helper.BuildingDashboardAdapter
import com.example.conferencerommapp.Helper.NetworkState
import com.example.conferencerommapp.R
import com.example.conferencerommapp.ViewModel.BuildingViewModel
import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.utils.GetProgress
import com.example.conferencerommapp.utils.ShowDialogForSessionExpired
import com.example.conferencerommapp.utils.ShowToast
import com.example.conferenceroomtabletversion.utils.GetPreference
import es.dmoral.toasty.Toasty

class BuildingDashboard : AppCompatActivity() {
    /**
     * Declaring Global variables and butterknife
     */
    @BindView(R.id.buidingRecyclerView)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.building_dashboard_progress_bar)
    lateinit var mProgressBar: ProgressBar
    private lateinit var buildingAdapter: BuildingDashboardAdapter
    private lateinit var mBuildingsViewModel: BuildingViewModel
    private lateinit var mProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_dashboard)
        ButterKnife.bind(this)
        init()
        observeData()
    }

    /**
     * onClick on this button goes to AddBuilding Activity
     */
    @OnClick(R.id.button_add_building)
    fun addBuildingFloatingButton() {
        startActivity(Intent(this, AddingBuilding::class.java).putExtra("FLAG",false))
    }

    /**
     * Restart the Activity
     */

    override fun onRestart() {
        super.onRestart()
        mBuildingsViewModel.getBuildingList(GetPreference.getTokenFromPreference(this))
    }

    /**
     * initialize objects
     */
    private fun init() {
        initActionBar()
        initLateInitializerVariables()
        if (NetworkState.appIsConnectedToInternet(this)) {
            getViewModel()
        } else {
            val i = Intent(this@BuildingDashboard, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }

    }
    private fun  initLateInitializerVariables() {
        mProgressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message_processing), this)
        mBuildingsViewModel = ViewModelProviders.of(this).get(BuildingViewModel::class.java)
    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title =
            Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Building_Dashboard) + "</font>")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            getViewModel()
        }
    }


    /**
     * observe data from server
     */
    private fun observeData() {
        mBuildingsViewModel.returnMBuildingSuccess().observe(this, Observer {
            mProgressBar.visibility = View.GONE
            if(it.isEmpty()) {
                Toasty.info(this, getString(R.string.please_add_building), Toasty.LENGTH_SHORT).show()
            } else {
                buildingAdapter = BuildingDashboardAdapter(this, it, object : BuildingDashboardAdapter.BtnClickListener {
                    override fun onBtnClick(buildingId: String?, buildingname: String?) {
                        val intent = Intent(this@BuildingDashboard, ConferenceDashBoard::class.java)
                        intent.putExtra(Constants.EXTRA_BUILDING_ID, buildingId)
                        startActivity(intent)
                    }
                },
                    object : BuildingDashboardAdapter.EditClickListener {
                        override fun onEditBtnClick(position: Int) {
                            val intent = Intent(this@BuildingDashboard, AddingBuilding::class.java)
                            intent.putExtra(Constants.BUILDING_ID, it[position].buildingId!!.toInt())
                            intent.putExtra(Constants.BUILDING_NAME, it[position].buildingName)
                            intent.putExtra(Constants.BUILDING_PLACE, it[position].buildingPlace)
                            intent.putExtra(Constants.FLAG, true)
                            startActivity(intent)
                        }
                    })
                recyclerView.adapter = buildingAdapter
            }
        })
        mBuildingsViewModel.returnMBuildingFailure().observe(this, Observer {
            mProgressBar.visibility = View.GONE
            if (it == Constants.INVALID_TOKEN) {
                ShowDialogForSessionExpired.showAlert(this, BuildingDashboard())
            } else {
                ShowToast.show(this, it as Int)
                finish()
            }
            //some message goes here
        })
    }

    /**
     * setting the adapter by passing the data into it and implementing a Interface BtnClickListner of BuildingAdapter class
     */
    private fun getViewModel() {
        mProgressBar.visibility = View.VISIBLE
        // making API call
        mBuildingsViewModel.getBuildingList(GetPreference.getTokenFromPreference(this))
    }
}

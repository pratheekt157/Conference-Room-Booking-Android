@file:Suppress("DEPRECATION")

package com.example.conferencerommapp.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.BuildingViewModel
import com.example.conferenceroomtabletversion.utils.GetPreference

class BuildingDashboard : AppCompatActivity() {
    /**
     * Declaring Global variables and butterknife
     */
    @BindView(R.id.buidingRecyclerView)
    lateinit var recyclerView: RecyclerView
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
        startActivity(Intent(this, AddingBuilding::class.java))
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
        val actionBar = supportActionBar
        actionBar!!.title =
            Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Building_Dashboard) + "</font>")
        mProgressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message_processing), this)
        mBuildingsViewModel = ViewModelProviders.of(this).get(BuildingViewModel::class.java)
        if (NetworkState.appIsConnectedToInternet(this)) {
            getViewModel()
        } else {
            val i = Intent(this@BuildingDashboard, NoInternetConnectionActivity::class.java)
            startActivityForResult(i, Constants.RES_CODE)
        }

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
            mProgressDialog.dismiss()
            buildingAdapter = BuildingDashboardAdapter(this, it, object : BuildingDashboardAdapter.BtnClickListener {
                override fun onBtnClick(buildingId: String?, buildingname: String?) {
                    val intent = Intent(this@BuildingDashboard, ConferenceDashBoard::class.java)
                    intent.putExtra(Constants.EXTRA_BUILDING_ID, buildingId)
                    startActivity(intent)
                }
            })
            recyclerView.adapter = buildingAdapter
        })
        mBuildingsViewModel.returnMBuildingFailure().observe(this, Observer {
            mProgressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
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
        mProgressDialog.show()
        // making API call
        mBuildingsViewModel.getBuildingList(GetPreference.getTokenFromPreference(this))
    }

    /**
     * show dialog for session expired
     */
    private fun showAlert() {
        var dialog = GetAleretDialog.getDialog(
            this, getString(R.string.session_expired), "Your session is expired!\n" +
                    getString(R.string.session_expired_messgae)
        )
        dialog.setPositiveButton(R.string.ok) { _, _ ->
            signOut()
        }
        var builder = GetAleretDialog.showDialog(dialog)
        ColorOfDialogButton.setColorOfDialogButton(builder)
    }

    /**
     * sign out from application
     */
    private fun signOut() {
        var mGoogleSignInClient = GoogleGSO.getGoogleSignInClient(this)
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                startActivity(Intent(applicationContext, SignIn::class.java))
                finish()
            }
    }


}

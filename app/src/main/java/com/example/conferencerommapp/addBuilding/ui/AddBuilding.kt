package com.example.conferencerommapp.addBuilding.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.conferencerommapp.BaseApplication
import com.example.conferencerommapp.R
import com.example.conferencerommapp.addBuilding.repository.AddBuildingRepository
import com.example.conferencerommapp.addBuilding.viewModel.AddBuild
import com.example.conferencerommapp.addBuilding.viewModel.AddBuildingListner
import com.example.conferencerommapp.databinding.ActivityAddingBuildingBinding
import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.utils.GetProgress
import com.example.conferencerommapp.utils.ShowDialogForSessionExpired
import com.example.conferencerommapp.utils.ShowToast
import es.dmoral.toasty.Toasty
import javax.inject.Inject

class AddBuilding:AppCompatActivity(),AddBuildingListner {

    @Inject
    lateinit var mAddBuildingRepository: AddBuildingRepository

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        val binding: ActivityAddingBuildingBinding = DataBindingUtil.setContentView(this,R.layout.activity_adding_building)
        val viewModel = ViewModelProviders.of(this).get(AddBuild::class.java)
        binding.addBuildingViewModel = viewModel
        viewModel.buildingListner =this
        viewModel.setBuildingRepository(mAddBuildingRepository)
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message_processing), this)

    }
    override fun onStarted() {
        progressDialog.show()
    }

    private fun initComponent() {
        (application as BaseApplication).getmAppComponent()?.inject(this)
    }

    override fun onSuccess(mSuccessForAddBuilding: MutableLiveData<Int>) {
        mSuccessForAddBuilding.observe(this, Observer {
            progressDialog.dismiss()
            Toasty.success(this, getString(R.string.building_added), Toast.LENGTH_SHORT, true).show()
            finish()
        })
    }

    override fun onFailure(mFailureForAddBuilding: MutableLiveData<Any>) {
        mFailureForAddBuilding.observe(this, Observer {
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                ShowDialogForSessionExpired.showAlert(this, AddingBuilding())
            } else {
                ShowToast.show(this, it as Int)
            }
        })
    }

    override fun onFailure(message: String) {
        progressDialog.dismiss()
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}
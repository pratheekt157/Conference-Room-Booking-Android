package com.example.conferencerommapp.addBuilding.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.model.AddBuilding
import com.example.conferencerommapp.addBuilding.repository.AddBuildingRepository
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.google.android.material.textfield.TextInputLayout
import androidx.databinding.BindingAdapter
import kotlinx.android.synthetic.main.activity_adding_building.view.*


class AddBuild : ViewModel() {

    var buildingName: String? = null
    var buildingPlace: String? = null

    var mAddBuildingRepository: AddBuildingRepository? = null

    private var mAddBuilding = AddBuilding()
    var buildingListner: AddBuildingListner? = null
    var mSuccessForAddBuilding = MutableLiveData<Int>()
    var mFailureForAddBuilding = MutableLiveData<Any>()


    fun setBuildingRepository(mAddBuildingRepository: AddBuildingRepository) {
        this.mAddBuildingRepository = mAddBuildingRepository
    }

    fun onAddBuildingButton(view: View) {

        if (buildingName.isNullOrEmpty()) {
            setErrorMessage(view.building_name_layout, "Error Message")
            //buildingListner!!.onFailure("Please Enter the Building Name")
            return
        }
        if (buildingPlace.isNullOrEmpty()) {
            //buildingListner!!.onFailure("Please Enter the Building Place")
            return
        }
        //success
        buildingListner?.onStarted()
        //addDatatoObject()
        mAddBuilding.buildingName = buildingName
        mAddBuilding.place = buildingPlace

        mAddBuildingRepository?.addBuildingDetails(mAddBuilding,GetPreference.getTokenFromPreference(view.context),object:ResponseListener{
            override fun onSuccess(success: Any) {
                mSuccessForAddBuilding.value = success as Int
                returnSuccessForAddBuilding()
            }

            override fun onFailure(failure: Any) {
                mFailureForAddBuilding.value = failure
                returnFailureForAddBuilding()
            }

        })

    }

    @BindingAdapter("app:errorText")
    fun setErrorMessage(view: TextInputLayout, errorMessage: String) {
        view.error = errorMessage
    }

    fun returnSuccessForAddBuilding() {
        buildingListner?.onSuccess(mSuccessForAddBuilding)

    }

    /**
     * return negative response from server
     */
    fun returnFailureForAddBuilding() {
        buildingListner?.onFailure(mFailureForAddBuilding)
    }
}
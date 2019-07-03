package com.example.conferencerommapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Model.AddBuilding
import com.example.conferencerommapp.Repository.AddBuildingRepository

open class AddBuildingViewModel : ViewModel() {

    /**
     * a object which will hold the reference to the corrosponding repository class
     */
    var mAddBuildingRepository: AddBuildingRepository? = null

    /**
     * a MutableLivedata variable which will hold the response from server
     */
    var mSuccessForAddBuilding = MutableLiveData<Int>()
    var mFailureForAddBuilding = MutableLiveData<Any>()

    var mSuccessForUpdateBuilding = MutableLiveData<Int>()
    var mFailureForUpdateBuilding = MutableLiveData<Any>()

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and assign values to the live data objects
     */
    fun addBuildingDetails(mAddBuilding: AddBuilding, token: String) {
        mAddBuildingRepository = AddBuildingRepository.getInstance()
        mAddBuildingRepository!!.addBuildingDetails(mAddBuilding, token, object :
            ResponseListener {
            override fun onFailure(failure: Any) {
                mFailureForAddBuilding.value = failure
            }

            override fun onSuccess(success: Any) {
                mSuccessForAddBuilding.value = success as Int
            }
        })
    }

    /**
     * return positive response from server
     */
    fun returnSuccessForAddBuilding(): MutableLiveData<Int> {
        return mSuccessForAddBuilding
    }
    /**
     * return negative response from server
     */
    fun returnFailureForAddBuilding(): MutableLiveData<Any> {
        return mFailureForAddBuilding
    }


    //-----------------------------------------update building details ----------------------------------------------------
    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and assign values to the live data objects
     */
    fun updateBuildingDetails(mAddBuilding: AddBuilding, token: String) {
        mAddBuildingRepository = AddBuildingRepository.getInstance()
        mAddBuildingRepository!!.updateBuildingDetails(mAddBuilding, token, object :
            ResponseListener {
            override fun onFailure(failure: Any) {
                mFailureForUpdateBuilding.value = failure
            }

            override fun onSuccess(success: Any) {
                mSuccessForUpdateBuilding.value = success as Int
            }
        })
    }

    /**
     * return positive response from server
     */
    fun returnSuccessForUpdateBuilding(): MutableLiveData<Int> {
        return mSuccessForUpdateBuilding
    }
    /**
     * return negative response from server
     */
    fun returnFailureForUpdateBuilding(): MutableLiveData<Any> {
        return mFailureForUpdateBuilding
    }

}

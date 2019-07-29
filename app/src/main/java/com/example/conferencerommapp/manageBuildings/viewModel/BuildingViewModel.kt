package com.example.conferencerommapp.manageBuildings.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.model.Building
import com.example.conferencerommapp.manageBuildings.repository.BuildingsRepository


class BuildingViewModel : ViewModel() {

    /**
     * a object which will hold the reference to the corresponding repository class
     */
    private var mBuildingsRepository: BuildingsRepository? = null

    /**
     * A MutableLivedata variable which will hold the Value for positive response from repository
     */
    var mBuildingList =  MutableLiveData<List<Building>>()

    /**
     * A MutableLiveData variable which will hold the Value for negative response from repository
     */
    var errorCodeFromServer =  MutableLiveData<Any>()

    var mSuccessForDeleteBuilding = MutableLiveData<Int>()

    var mFailureForDeleteBuilding = MutableLiveData<Any>()

    fun setBuildingRepository(buildingsRepository: BuildingsRepository) {
        this.mBuildingsRepository = buildingsRepository
    }

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will update the values of MutableLiveData objects according to the response from server
     */
    fun getBuildingList(token: String) {
        mBuildingsRepository?.getBuildingList(token, object: ResponseListener {
            override fun onFailure(failure: Any) {
                    errorCodeFromServer.value = failure
            }
            override fun onSuccess(success: Any) {
                mBuildingList.value = success as List<Building>
            }
        })
    }

    /**
     * function will return the MutableLiveData of List of buildings
     */

    fun returnMBuildingSuccess(): MutableLiveData<List<Building>> {
        return mBuildingList
    }

    /**
     * function will return the MutableLiveData of Int if something went wrong at server
     */
    fun returnMBuildingFailure(): MutableLiveData<Any> {
        return errorCodeFromServer
    }

    fun deleteBuilding(token: String,id:Int) {
        mBuildingsRepository?.deleteBuilding(token,id, object: ResponseListener {
            override fun onFailure(failure: Any) {
                mFailureForDeleteBuilding.value = failure
            }
            override fun onSuccess(success: Any) {
                mSuccessForDeleteBuilding.value = success as Int
            }

        })
    }

    /**
     * return positive response from server
     */
    fun returnSuccessForDeleteBuilding(): MutableLiveData<Int> {
        return mSuccessForDeleteBuilding
    }
    /**
     * return negative response from server
     */
    fun returnFailureForDeleteBuilding(): MutableLiveData<Any> {
        return mFailureForDeleteBuilding
    }


}
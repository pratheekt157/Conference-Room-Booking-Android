package com.example.conferencerommapp.addBuilding.viewModel

import androidx.lifecycle.MutableLiveData

interface AddBuildingListner {
    fun onStarted()
    fun onSuccess(mSuccessForAddBuilding: MutableLiveData<Int>)
    fun onFailure(mFailureForAddBuilding: MutableLiveData<Any>)
    fun onFailure(message:String)
}
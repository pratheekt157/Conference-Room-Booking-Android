package com.example.conferencerommapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Repository.CheckRegistrationRepository

class CheckRegistrationViewModel : ViewModel() {

    /**
     * a object which will hold the reference to the corrosponding repository class
     */
    var mCheckRegistrationRepository: CheckRegistrationRepository? = null

    /**
     * a MutableLivedata variable which will hold the Value for the Livedata
     */
    var mSuccessCode =  MutableLiveData<Int>()
    var mFailureCode =  MutableLiveData<Any>()

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun checkRegistration(token: String, deviceId: String){
        mCheckRegistrationRepository = CheckRegistrationRepository.getInstance()
        mCheckRegistrationRepository!!.checkRegistration(token, deviceId, object:
            ResponseListener {
            override fun onSuccess(success: Any) {
                mSuccessCode.value = success as Int
            }

            override fun onFailure(failure: Any) {
                mFailureCode.value = failure
            }

        })
    }

    fun returnSuccessCode(): MutableLiveData<Int> {
        return mSuccessCode
    }
    fun returnFailureCode(): MutableLiveData<Any> {
        return mFailureCode
    }

    //-----------------------------------------------User Registration
}

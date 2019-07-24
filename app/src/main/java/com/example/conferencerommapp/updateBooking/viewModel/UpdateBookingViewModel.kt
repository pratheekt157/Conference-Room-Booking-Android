package com.example.conferencerommapp.updateBooking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.Model.UpdateBooking
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.updateBooking.repository.UpdateBookingRepository


class UpdateBookingViewModel: ViewModel() {

    /**
     * a object which will hold the reference to the corrosponding repository class
     */
    private var mUpdateBookingRepository: UpdateBookingRepository? = null

    /**
     * a MutableLivedata variable which will hold the positive response from repository
     */
    var mSuccessForUpdate =  MutableLiveData<Int>()

    /**
     * a MutableLivedata variable which will hold the positive response from repository
     */
    var mFailureForUpdate =  MutableLiveData<Any>()

    fun setUpdateBookingRepo(mUpdatedetailsRepo: UpdateBookingRepository) {
        mUpdateBookingRepository = mUpdatedetailsRepo
    }

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun updateBookingDetails(mUpdateBooking: UpdateBooking, token: String) {
        mUpdateBookingRepository!!.updateBookingDetails(mUpdateBooking, token, object:
            ResponseListener {
            override fun onSuccess(success: Any) {
                mSuccessForUpdate.value = success as Int
            }

            override fun onFailure(failure: Any) {
                mFailureForUpdate.value = failure
            }

        })
    }

    /**
     * function will return the MutableLiveData of Int
     */
    fun returnBookingUpdated(): MutableLiveData<Int> {
        return mSuccessForUpdate
    }

    /**
     * function will return the MutableLiveData of Int if something went wrong at server
     */
    fun returnUpdateFailed(): MutableLiveData<Any> {
        return mFailureForUpdate
    }
}
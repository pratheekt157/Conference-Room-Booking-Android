package com.example.conferencerommapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Model.Booking
import com.example.conferencerommapp.Repository.BookingRepository

class BookingViewModel: ViewModel() {

    /**
     * a object which will hold the reference to the corrosponding repository class
     */
    private var mBookingRepository: BookingRepository? = null

    /**
     * a MutableLivedata variable which will hold the Value for the Livedata
     */
    var mSuccessForBooking =  MutableLiveData<Int>()

    var mErrorCodeFromServerFromBooking =  MutableLiveData<Any>()


    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun addBookingDetails(mBooking: Booking, token: String) {
        mBookingRepository!!.addBookingDetails(mBooking, token, object:
            ResponseListener {
            override fun onFailure(failure: Any) {
                mErrorCodeFromServerFromBooking.value = failure
            }
            override fun onSuccess(success: Any) {
                mSuccessForBooking.value = success as Int
            }

        })
    }

    /**
     * function will return MutableLiveData of List of EmployeeList
     */
    fun returnSuccessForBooking(): MutableLiveData<Int> {
        return mSuccessForBooking
    }

    /**
     * function will return the MutableLiveData of Int if something went wrong at server
     */
    fun returnFailureForBooking(): MutableLiveData<Any> {
        return mErrorCodeFromServerFromBooking
    }
}
package com.example.conferencerommapp.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.Helper.ResponseListener
import com.example.conferencerommapp.Model.*
import com.example.conferencerommapp.Repository.BookingDashboardRepository
import com.example.conferencerommapp.Repository.UpdateBookingRepository

class BookingDashboardViewModel : ViewModel() {

    /**
     * a object which will hold the reference to the corrosponding repository class
     */
    var mBookingDashboardRepository: BookingDashboardRepository? = null

    /**
     * a MutableLivedata variables which will hold the Positive and Negative response from server
     */
    private var mBookingList = MutableLiveData<DashboardDetails>()

    private var mFailureCodeForBookingList = MutableLiveData<Any>()

    /**
     * a MutableLivedata variable which will hold the Value for the Livedata
     */
    var mSuccessForCancelBooking = MutableLiveData<Int>()
    var mFailureForCancelBooking = MutableLiveData<Any>()

    // mutablelivedata variable which will hold response from server for passcode
    var mSuccessForPasscode = MutableLiveData<Int>()
    var mFailureForPasscode = MutableLiveData<Any>()
    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun getBookingList(token: String, mBookingDashboardInput: BookingDashboardInput) {
        mBookingDashboardRepository = BookingDashboardRepository.getInstance()
        mBookingDashboardRepository!!.getBookingList(
            token,
            mBookingDashboardInput,
            object : ResponseListener {
                override fun onSuccess(success: Any) {
                    mBookingList.value = success as DashboardDetails
                }

                override fun onFailure(failure: Any) {
                    mFailureCodeForBookingList.value = failure
                }

            })
    }

    /**
     * function will return the MutableLiveData of List of dashboard
     */
    fun returnSuccess(): MutableLiveData<DashboardDetails> {
        return mBookingList
    }

    /**
     * function will return the MutableLiveData of Int if something went wrong at server
     */
    fun returnFailure(): MutableLiveData<Any> {
        return mFailureCodeForBookingList
    }



//----------------------------------------------------------------------------------------------------------------------

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun cancelBooking(token: String, meetingId: Int) {
        mBookingDashboardRepository = BookingDashboardRepository.getInstance()
        mBookingDashboardRepository!!.cancelBooking(token, meetingId, object : ResponseListener {
            override fun onFailure(failure: Any) {
                mFailureForCancelBooking.value = failure
            }

            override fun onSuccess(success: Any) {
                mSuccessForCancelBooking.value = success as Int
            }

        })
    }

    /**
     * function will return the MutableLiveData of Int
     */
    fun returnBookingCancelled(): MutableLiveData<Int> {
        return mSuccessForCancelBooking
    }

    /**
     * function will return the MutableLiveData of Int if something went wrong at server
     */
    fun returnCancelFailed(): MutableLiveData<Any> {
        return mFailureForCancelBooking
    }



    //--------------------------------------------------------------------------------------------------------------

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun getPasscode(token: String) {
        mBookingDashboardRepository = BookingDashboardRepository.getInstance()
        mBookingDashboardRepository!!.getPasscode(token, object : ResponseListener {
            override fun onFailure(failure: Any) {
                mFailureForPasscode.value = failure
            }

            override fun onSuccess(success: Any) {
                mSuccessForPasscode.value = success as Int
            }

        })
    }

    /**
     * function will return the MutableLiveData of Int
     */
    fun returnPasscode(): MutableLiveData<Int> {
        return mSuccessForPasscode
    }

    /**
     * function will return the MutableLiveData of Int if something went wrong at server
     */
    fun returnPasscodeFailed(): MutableLiveData<Any> {
        return mFailureForPasscode
    }
}
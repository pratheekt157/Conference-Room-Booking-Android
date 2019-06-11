package com.example.conferencerommapp.Repository

import android.util.Log
import com.example.conferencerommapp.Helper.Constants
import com.example.conferencerommapp.Helper.ResponseListener
import com.example.conferencerommapp.Model.BookingDashboardInput
import com.example.conferencerommapp.Model.Dashboard
import com.example.conferencerommapp.Model.DashboardDetails
import com.example.conferencerommapp.services.ConferenceService
import com.example.conferencerommapp.utils.GetCurrentTimeInUTC
import com.example.globofly.services.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class BookingDashboardRepository {

    /**
     * this block provides a static method which will return the object of repository
     * if the object is already their than it return the same
     * or else it will return a new object
     */
    companion object {
        private var mBookingDashboardRepository: BookingDashboardRepository? = null
        fun getInstance(): BookingDashboardRepository {
            if (mBookingDashboardRepository == null) {
                mBookingDashboardRepository = BookingDashboardRepository()
            }
            return mBookingDashboardRepository!!
        }
    }


    /**
     * function will make api call for making a booking
     * and call the interface method with data from server
     */
    fun getBookingList(token: String, mBookingDashboardInput: BookingDashboardInput, listener: ResponseListener) {
        /**
         * API call using retrofit
         */

        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var dateDate = dateFormat.parse(dateFormat.format(date))
        Log.d("date------", dateDate.toString())

        mBookingDashboardInput.currentDatTime = GetCurrentTimeInUTC.getCurrentTimeInUTC()
        val service = ServiceBuilder.getObject()
        val requestCall: Call<DashboardDetails> = service.getDashboard(token, mBookingDashboardInput)
        requestCall.enqueue(object : Callback<DashboardDetails> {
            override fun onFailure(call: Call<DashboardDetails>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
            }

            override fun onResponse(call: Call<DashboardDetails>, response: Response<DashboardDetails>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }
        })

    }

    /**
     * function will make the API Call and call the interface method with data from server
     */
    fun cancelBooking(token: String, meetingId: Int, listener: ResponseListener) {
        /**
         * api call using retrofit
         */
        val service = ServiceBuilder.getObject()
        var requestCall: Call<ResponseBody> = service.cancelBookedRoom(token, meetingId)
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onFailure(Constants.INVALID_TOKEN)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.code())
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }

    /**
     * function will make the API Call and call the interface method with data from srver
     */
    fun recurringCancelBooking(token: String,meetId:Int,recurringMeetingId:String,listener: ResponseListener){
        /**
         * api call using rerofit
         */
        val service:ConferenceService = ServiceBuilder.getObject()
        val requestCall: Call<ResponseBody> = service.cancelRecurringBooking(token,meetId,recurringMeetingId)
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onFailure(Constants.INVALID_TOKEN)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.code())
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }
    fun getPasscode(token: String, listener: ResponseListener) {
        /**
         * api call using retrofit
         */
        val service = ServiceBuilder.getObject()
        var requestCall: Call<Int> = service.getPasscode(token)
        requestCall.enqueue(object : Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                listener.onFailure(Constants.INVALID_TOKEN)
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }
}






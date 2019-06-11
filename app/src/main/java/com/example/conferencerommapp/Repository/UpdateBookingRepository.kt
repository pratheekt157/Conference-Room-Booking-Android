package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Model.UpdateBooking
import com.example.globofly.services.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateBookingRepository {
    companion object {
        var mUpdateBookingRepository: UpdateBookingRepository? = null
        fun getInstance(): UpdateBookingRepository {
            if (mUpdateBookingRepository == null) {
                mUpdateBookingRepository = UpdateBookingRepository()
            }
            return mUpdateBookingRepository!!
        }
    }

    /**
     * function will make an API call to make request for the updation of booking
     * and call the interface method with data from server
     */
    fun updateBookingDetails(mUpdateBooking: UpdateBooking, token: String, listener: ResponseListener) {
        val service = ServiceBuilder.getObject()
        val requestCall: Call<ResponseBody> = service.update(token, mUpdateBooking)
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
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
}
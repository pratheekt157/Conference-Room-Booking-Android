package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.globofly.services.ServiceBuilder
import com.example.myapplication.Models.ConferenceList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HrConferenceRoomRepository {

    companion object {
        private var mHrConferenceRoomRepository: HrConferenceRoomRepository? = null
        fun getInstance(): HrConferenceRoomRepository {
            if (mHrConferenceRoomRepository == null) {
                mHrConferenceRoomRepository = HrConferenceRoomRepository()
            }
            return mHrConferenceRoomRepository!!
        }
    }

    fun getConferenceRoomList(buildingId: Int, token: String,  listener: ResponseListener) {
        /**
         * api call using retorfit
         */
        val service = ServiceBuilder.getObject()
        val requestCall: Call<List<ConferenceList>> = service.conferencelist(token, buildingId)
        requestCall.enqueue(object : Callback<List<ConferenceList>> {
            override fun onFailure(call: Call<List<ConferenceList>>, t: Throwable) {
                when(t) {
                    is SocketTimeoutException -> {
                        listener.onFailure(Constants.POOR_INTERNET_CONNECTION)
                    }
                    is UnknownHostException -> {
                        listener.onFailure(Constants.POOR_INTERNET_CONNECTION)
                    }
                    else -> {
                        listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
                    }
                }
            }

            override fun onResponse(call: Call<List<ConferenceList>>, response: Response<List<ConferenceList>>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }
}
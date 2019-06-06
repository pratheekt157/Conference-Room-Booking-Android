package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.Helper.Constants
import com.example.conferencerommapp.Helper.ResponseListener
import com.example.globofly.services.ServiceBuilder
import com.example.myapplication.Models.ConferenceList
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
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
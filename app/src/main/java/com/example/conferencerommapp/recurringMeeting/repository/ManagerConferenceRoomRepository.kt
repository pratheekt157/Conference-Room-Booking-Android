package com.example.conferencerommapp.recurringMeeting.repository

import com.example.conferencerommapp.Model.ManagerConference
import com.example.conferencerommapp.Model.RoomDetails
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.services.RestClient
import com.example.conferencerommapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ManagerConferenceRoomRepository @Inject constructor() {
    /**
     * function will initialize the MutableLivedata Object and than call a function for api call
     * Passing the Context and model and call API, In return sends the status of LiveData
     */
    fun getConferenceRoomList(mRoom: ManagerConference, token: String, listener: ResponseListener) {
        /**
         * api call using retrofit
         */
        val requestCall: Call<List<RoomDetails>> =
            RestClient.getWebServiceData()?.getMangerConferenceRoomList(token, mRoom)!!
        requestCall.enqueue(object : Callback<List<RoomDetails>> {
            override fun onFailure(call: Call<List<RoomDetails>>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
            }

            override fun onResponse(call: Call<List<RoomDetails>>, response: Response<List<RoomDetails>>) {

                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }


    /**
     * function will initialize the MutableLivedata Object and than make API Call
     * Passing the Context and model and call API, In return sends the status of LiveData
     */
    fun getSuggestedRooms(token: String, mInputDetailsForRoom: ManagerConference, listener: ResponseListener) {
        /**
         * api call using Retrofit
         */
        val requestCall: Call<List<RoomDetails>> =
            RestClient.getWebServiceData()?.getSuggestedRoomsForRecurring(token, mInputDetailsForRoom)!!
        requestCall.enqueue(object : Callback<List<RoomDetails>> {
            override fun onFailure(call: Call<List<RoomDetails>>, t: Throwable) {
                when (t) {
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

            override fun onResponse(call: Call<List<RoomDetails>>, response: Response<List<RoomDetails>>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }
}



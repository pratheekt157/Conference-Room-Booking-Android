package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.Model.InputDetailsForRoom
import com.example.conferencerommapp.Model.RoomDetails
import com.example.conferencerommapp.ServiceBuilder
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import com.example.conferencerommapp.Repository.ConferenceRoomRepository as ConferenceRoomRepository1

class ConferenceRoomRepository {

    /**
     * this block provides a static method which will return the object of repository
     * if the object is already their than it return the same
     * or else it will return a new object
     */
    companion object {
        private var mConferenceRoomRepository: ConferenceRoomRepository1? = null
        fun getInstance(): ConferenceRoomRepository1 {
            if (mConferenceRoomRepository == null) {
                mConferenceRoomRepository = ConferenceRoomRepository1()
            }
            return mConferenceRoomRepository!!
        }
    }

    /**
     * function will initialize the MutableLivedata Object and than make API Call
     * Passing the Context and model and call API, In return sends the status of LiveData
     */
    fun getConferenceRoomList(token: String, mInputDetailsForRoom: InputDetailsForRoom, listener: ResponseListener) {
        /**
         * api call using Retrofit
         */
        val service = ServiceBuilder.getObject()
        val requestCall: Call<List<RoomDetails>> = service.getConferenceRoomList(token, mInputDetailsForRoom)
        requestCall.enqueue(object : Callback<List<RoomDetails>> {
            override fun onFailure(call: Call<List<RoomDetails>>, t: Throwable) {
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
    fun getSuggestedRooms(token: String, mInputDetailsForRoom: InputDetailsForRoom, listener: ResponseListener) {
        /**
         * api call using Retrofit
         */
        val service = ServiceBuilder.getObject()
        val requestCall: Call<List<RoomDetails>> = service.getSuggestedRooms(token, mInputDetailsForRoom)
        requestCall.enqueue(object : Callback<List<RoomDetails>> {
            override fun onFailure(call: Call<List<RoomDetails>>, t: Throwable) {
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

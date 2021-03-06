package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.AddConferenceRoom
import com.example.conferencerommapp.ServiceBuilder
import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class AddConferenceRepository {

    companion object {
        var mAddConferenceRepository: AddConferenceRepository? = null
        fun getInstance():AddConferenceRepository{
            if(mAddConferenceRepository == null){
                mAddConferenceRepository = AddConferenceRepository()
            }
            return mAddConferenceRepository!!
        }
    }

    //Passing the Context and model and call API, In return sends the status of LiveData
    fun addConferenceDetails(mConferenceRoom : AddConferenceRoom, token: String, listener: ResponseListener) {
        //Retrofit Call
        val addConferenceRoomService = ServiceBuilder.getObject()
        val addConferenceRequestCall: Call<ResponseBody> = addConferenceRoomService.addConference(token, mConferenceRoom)

        addConferenceRequestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.code())
                }else {
                    listener.onFailure(response.code())
                }
            }
        })
    }

    // ------------------------------------------------update Room Details --------------------------------------
    //Passing the Context and model and call API, In return sends the status of LiveData
    fun updateConferenceDetails(mConferenceRoom : AddConferenceRoom, token: String, listener: ResponseListener) {
        //Retrofit Call
        val addConferenceRoomService = ServiceBuilder.getObject()
        val addConferenceRequestCall: Call<ResponseBody> = addConferenceRoomService.updateConference(token, mConferenceRoom)
        addConferenceRequestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.code())
                }else {
                    listener.onFailure(response.code())
                }
            }
        })
    }

}
package com.example.conferencerommapp.ConferenceRoomDashboard.repository

import com.example.conferencerommapp.ServiceBuilder
import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.services.RestClient
import com.example.myapplication.Models.ConferenceList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ManageConferenceRoomRepository @Inject constructor() {

    fun getConferenceRoomList(buildingId: Int, token: String,  listener: ResponseListener) {
        /**
         * api call using retorfit
         */
        val requestCall: Call<List<ConferenceList>> = RestClient.getWebServiceData()?.conferenceList(token, buildingId)!!
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

    /**
     * function will initialize the MutableLiveData Object and than  make API Call
     * if the response is positive than we will call onSuccess method with response data from server
     * for negative response, we will call onFailure method with response code from server
     */
    fun deleteBuilding(token: String, id:Int,listener: ResponseListener){
        val requestCall: Call<ResponseBody> = RestClient.getWebServiceData()?.deleteRoom(token,id)!!
        requestCall.enqueue(object :Callback<ResponseBody>{
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
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.NO_CONTENT_FOUND)) {
                    /**
                     * call interface method which is implemented in ViewModel
                     */
                    listener.onSuccess(response.code())
                }
                else
                    listener.onFailure(response.code())
            }

        })
    }
}
package com.example.conferencerommapp.blockDashboard.repository

import com.example.conferencerommapp.Blocked
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.services.RestClient
import com.example.conferencerommapp.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class BlockDashboardRepository @Inject constructor(){
    fun getBlockedList(token: String, listener: ResponseListener) {
        val requestCall: Call<List<Blocked>> = RestClient.getWebServiceData()?.getBlockedConference(token)!!
        requestCall.enqueue(object : Callback<List<Blocked>> {
            override fun onFailure(call: Call<List<Blocked>>, t: Throwable) {
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
            override fun onResponse(call: Call<List<Blocked>>, response: Response<List<Blocked>>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }

        })
    }

    /**
     * make request to server for unblock room
     */
    fun unblockRoom(token: String, bookingId: Int, listener: ResponseListener) {
        val requestCall: Call<ResponseBody> = RestClient.getWebServiceData()?.unBlockingConferenceRoom(token, bookingId)!!
        requestCall.enqueue(object : Callback<ResponseBody> {
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
                } else {
                    listener.onFailure(response.code())
                }

            }
        })
    }
}
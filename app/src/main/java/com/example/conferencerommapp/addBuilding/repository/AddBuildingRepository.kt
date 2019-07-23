package com.example.conferencerommapp.addBuilding.repository

import com.example.conferencerommapp.Model.AddBuilding
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


class AddBuildingRepository @Inject constructor(){

    /**
     * make API call and calls the methods of interface
     */
    fun addBuildingDetails(mAddBuilding: AddBuilding, token: String, listener: ResponseListener) {
        val addBuildingRequestCall: Call<ResponseBody> = RestClient.getWebServiceData()?.addBuilding(token, mAddBuilding)!!
        addBuildingRequestCall.enqueue(object : Callback<ResponseBody> {
            // Negative response
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
            //positive response
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.code())
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }
    //--------------------------------------------api call for update building details ----------------------------

    /**
     * make API call and calls the methods of interface
     */
    fun updateBuildingDetails(mAddBuilding: AddBuilding, token: String, listener: ResponseListener) {
        val addBuildingRequestCall: Call<ResponseBody> = RestClient.getWebServiceData()?.updateBuilding(token, mAddBuilding)!!
        addBuildingRequestCall.enqueue(object : Callback<ResponseBody> {
            // Negative response
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
            //positive response
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

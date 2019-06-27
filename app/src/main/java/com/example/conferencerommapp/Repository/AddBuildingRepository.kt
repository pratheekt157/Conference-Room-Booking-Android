package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Model.AddBuilding
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class AddBuildingRepository {

    /**
     * this block provides a static method which will return the object of repository
     * if the object is already their than it return the same
     * or else it will return a new object
     */
    companion object {
        private var mAddBuildingRepository: AddBuildingRepository? = null
        fun getInstance(): AddBuildingRepository {
            if (mAddBuildingRepository == null) {
                mAddBuildingRepository = AddBuildingRepository()
            }
            return mAddBuildingRepository!!
        }
    }

    /**
     * make API call and calls the methods of interface
     */
    fun addBuildingDetails(mAddBuilding: AddBuilding, token: String, listener: ResponseListener) {
        val addBuildingService: ConferenceService = ServiceBuilder.getObject()
        val addBuildingRequestCall: Call<ResponseBody> = addBuildingService.addBuilding(token, mAddBuilding)
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
        val addBuildingService: ConferenceService = ServiceBuilder.getObject()
        val addBuildingRequestCall: Call<ResponseBody> = addBuildingService.updateBuilding(token, mAddBuilding)
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

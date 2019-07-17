package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.ServiceBuilder
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GetRoleOfUser {


    /**
     * this block provides a static method which will return the object of repository
     * if the object is already their than it return the same
     * or else it will return a new object
     */
    companion object{
        private var mGetRoleOfUser: GetRoleOfUser? = null
        fun getInstance(): GetRoleOfUser {
            if(mGetRoleOfUser == null) {
                mGetRoleOfUser = GetRoleOfUser()
            }
            return mGetRoleOfUser!!
        }
    }

    /**
     * function will initialize the MutableLivedata Object and than call a function for api call
     */
    fun getRole(token: String, email: String, listener: ResponseListener)  {
        /**
         * api call using retrofit
         */
        val service = ServiceBuilder.getObject()
        val requestCall: Call<Int> = service.getRole(token, email)
        requestCall.enqueue(object : Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
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
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }






}
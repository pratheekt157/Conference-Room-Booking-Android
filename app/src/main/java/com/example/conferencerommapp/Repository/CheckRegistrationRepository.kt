package com.example.conferencerommapp.Repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.conferencerommapp.Helper.Constants
import com.example.conferencerommapp.Helper.ResponseListener
import com.example.globofly.services.ServiceBuilder
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckRegistrationRepository {


    /**
     * this block provides a static method which will return the object of repository
     * if the object is already their than it return the same
     * or else it will return a new object
     */
    companion object{
        private var mCheckRegistrationRepository: CheckRegistrationRepository? = null
        fun getInstance(): CheckRegistrationRepository {
            if(mCheckRegistrationRepository == null) {
                mCheckRegistrationRepository = CheckRegistrationRepository()
            }
            return mCheckRegistrationRepository!!
        }
    }

    /**
     * function will initialize the MutableLivedata Object and than call a function for api call
     */
    fun checkRegistration(token: String, deviceId: String, listener: ResponseListener)  {
        /**
         * api call using retrofit
         */

        val service = ServiceBuilder.getObject()
        val requestCall: Call<Int> = service.getRequestCode(token, deviceId)
        requestCall.enqueue(object : Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
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

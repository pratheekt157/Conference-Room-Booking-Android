package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.Helper.Constants
import com.example.conferencerommapp.Helper.ResponseListener
import com.example.conferencerommapp.Model.Employee
import com.example.globofly.services.ServiceBuilder
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationRepository {

    companion object {

        private var mRegistrationRepository: RegistrationRepository? = null
        fun getInstance(): RegistrationRepository {
            if (mRegistrationRepository == null) {
                mRegistrationRepository = RegistrationRepository()
            }
            return mRegistrationRepository!!
        }
    }

    /**
     * Passing the Context and model and call API, In return sends the status of LiveData
     */
    fun addEmployee(mEmployee: Employee,userId: String, token: String,  listener: ResponseListener) {
        /**
         * Retrofit Call
         */
        val service = ServiceBuilder.getObject()
        val requestCall: Call<ResponseBody> = service.addEmployee(token, userId, mEmployee)
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
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
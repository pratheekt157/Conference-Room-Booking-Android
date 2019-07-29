package com.example.conferencerommapp.booking.repository

import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.model.EmployeeList
import com.example.conferencerommapp.services.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EmployeeRepository @Inject constructor(){

    /**
     * function will call the api which will return some data
     */
    fun getEmployeeList(token: String, email: String, listener: ResponseListener) {
        val requestCall: Call<List<EmployeeList>> = RestClient.getWebServiceData()?.getEmployees(token, email)!!
        requestCall.enqueue(object : Callback<List<EmployeeList>> {
            override fun onFailure(call: Call<List<EmployeeList>>, t: Throwable) {
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

            override fun onResponse(call: Call<List<EmployeeList>>, response: Response<List<EmployeeList>>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }

        })
    }
}


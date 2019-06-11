package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Model.EmployeeList
import com.example.globofly.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeRepository {

    /**
     * this block provides a static method which will return the object of repository
     * if the object is already their than it return the same
     * or else it will return a new object
     */
    companion object {
        private var mEmployeeRepository: EmployeeRepository? = null
        fun getInstance(): EmployeeRepository {
            if (mEmployeeRepository == null) {
                mEmployeeRepository = EmployeeRepository()
            }
            return mEmployeeRepository!!
        }
    }

    /**
     * function will call the api which will return some data
     */
    fun getEmployeeList(token: String, listener: ResponseListener) {
        val service = ServiceBuilder.getObject()
        val requestCall: Call<List<EmployeeList>> = service.getEmployees(token)
        requestCall.enqueue(object : Callback<List<EmployeeList>> {
            override fun onFailure(call: Call<List<EmployeeList>>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
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


package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.Model.Building
import com.example.conferencerommapp.ServiceBuilder
import com.example.conferencerommapp.services.ConferenceService
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class BuildingsRepository {

    /**
     * this block provides a static method which will return the object of repository
     * if the object is already their than it return the same
     * or else it will return a new object
     */
    companion object {
        var mBuildingsRepository: BuildingsRepository? = null
        fun getInstance(): BuildingsRepository {
            if (mBuildingsRepository == null) {
                mBuildingsRepository = BuildingsRepository()
            }
            return mBuildingsRepository!!
        }
    }

    /**
     * function will initialize the MutableLiveData Object and than  make API Call
     * if the response is positive than we will call onSuccess method with response data from server
     * for negative response, we will call onFailure method with response code from server
     */
    fun getBuildingList(token: String, listener: ResponseListener) {
        val service = ServiceBuilder.getObject()
        val requestCall: Call<List<Building>> = service.getBuildingList(token)
        requestCall.enqueue(object : Callback<List<Building>> {
            override fun onFailure(call: Call<List<Building>>, t: Throwable) {
                /**
                 * call interface method which is implemented in ViewModel
                 */
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
            override fun onResponse(call: Call<List<Building>>, response: Response<List<Building>>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.NO_CONTENT_FOUND)) {
                    /**
                     * call interface method which is implemented in ViewModel
                     */
                    if(response.body().isNullOrEmpty()) {
                        listener.onSuccess(ArrayList<Building>())
                    } else {
                        listener.onSuccess(response.body()!!)
                    }
                }else {
                    /**
                     * call interface method which is implemented in ViewModel
                     */
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
         val service = ServiceBuilder.getObject()
         val requestCall: Call<ResponseBody> = service.deleteBuilding(token,id)
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


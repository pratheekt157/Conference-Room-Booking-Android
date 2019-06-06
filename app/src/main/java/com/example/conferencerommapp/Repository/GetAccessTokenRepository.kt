package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.Helper.Constants
import com.example.conferencerommapp.Helper.ResponseListener
import com.example.conferencerommapp.Model.RefreshToken
import com.example.globofly.services.ServiceBuilder
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAccessTokenRepository {
    /**
     * this block provides a static method which will return the object of repository
     * if the object is already their than it return the same
     * or else it will return a new object
     */
    companion object {
        var mGetAccessTokenRepo: GetAccessTokenRepository? = null
        fun getInstance(): GetAccessTokenRepository {
            if (mGetAccessTokenRepo == null) {
                mGetAccessTokenRepo = GetAccessTokenRepository()
            }
            return mGetAccessTokenRepo!!
        }
    }

    /**
     * function will initialize the MutableLiveData Object and than  make API Call
     * if the response is positive than we will call onSuccess method with response data from server
     * for negative response, we will call onFailure method with response code from server
     */
    fun getAccessToken(mRefreshToken: RefreshToken, listener: ResponseListener) {
        val service = ServiceBuilder.getObject()
        val requestCall: Call<RefreshToken> = service.getAccessToken(mRefreshToken)
        requestCall.enqueue(object : Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>, t: Throwable) {
                /**
                 * call interface method which is implemented in ViewModel
                 */
                listener.onFailure("Internal Server Error!")
            }
            override fun onResponse(call: Call<RefreshToken>, response: Response<RefreshToken>) {
                if (response.code() == Constants.OK_RESPONSE) {
                    /**
                     * call interface method which is implemented in ViewModel
                     */
                    listener.onSuccess(response.body()!!)
                }else {
                    /**
                     * call interface method which is implemented in ViewModel
                     */
                    listener.onFailure(JSONObject(response.errorBody()!!.string()).getString("Message"))
                }

            }

        })
    }
}
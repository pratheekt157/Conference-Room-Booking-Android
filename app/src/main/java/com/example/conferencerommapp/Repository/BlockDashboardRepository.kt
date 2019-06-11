package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.Blocked
import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlockDashboardRepository {
    companion object {
        var mBlockedDashboardRepository: BlockDashboardRepository? = null

        fun getInstance(): BlockDashboardRepository {
            if (mBlockedDashboardRepository == null) {
                mBlockedDashboardRepository = BlockDashboardRepository()
            }
            return mBlockedDashboardRepository!!
        }
    }

    fun getBlockedList(token: String, listener: ResponseListener) {
        val blockServices  = ServiceBuilder.getObject()
        val requestCall: Call<List<Blocked>> = blockServices.getBlockedConference(token)
        requestCall.enqueue(object : Callback<List<Blocked>> {
            override fun onFailure(call: Call<List<Blocked>>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
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
        val unBlockApi = ServiceBuilder.buildService(ConferenceService::class.java)
        val requestCall: Call<ResponseBody> = unBlockApi.unBlockingConferenceRoom(token, bookingId)
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.code()!!)
                } else {
                    listener.onFailure(response.code())
                }

            }
        })
    }
}
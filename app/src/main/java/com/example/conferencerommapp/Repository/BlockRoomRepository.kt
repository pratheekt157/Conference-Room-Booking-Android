package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Model.BlockRoom
import com.example.conferencerommapp.Model.BlockingConfirmation
import com.example.globofly.services.ServiceBuilder
import com.example.myapplication.Models.ConferenceList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class BlockRoomRepository {
    companion object {
        private var mBlockRoomRepository: BlockRoomRepository? = null
        fun getInstance(): BlockRoomRepository {
            if (mBlockRoomRepository == null) {
                mBlockRoomRepository = BlockRoomRepository()
            }
            return mBlockRoomRepository!!
        }
    }

    /**
     *  function will make API call
     */
    fun blockRoom(mRoom: BlockRoom, token: String, listener: ResponseListener) {

        /**
         * make API call usnig retrofit
         */
        val blockRoomApi = ServiceBuilder.getObject()
        val requestCall: Call<ResponseBody> = blockRoomApi.blockconference(token, mRoom)
        requestCall.enqueue(object : Callback<ResponseBody> {
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
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                   listener.onSuccess(response.code())
                } else {
                    listener.onFailure(response.code())
                }
            }

        })
    }



    /**
     * ---------------------------------------------------------------------------------------------------------------------------
     */

    /**
     * function will initialize the MutableLivedata Object and than make API Call
     * Passing the Context and model and call API, In return sends the status of LiveData
     */
    fun getRoomList(buildingId: Int, token: String, listener: ResponseListener) {

        /**
         *  api call using retrofit
         */
        val requestCall: Call<List<ConferenceList>> = ServiceBuilder.getObject().conferenceList(token, buildingId)
        requestCall.enqueue(object : Callback<List<ConferenceList>> {
            override fun onFailure(call: Call<List<ConferenceList>>, t: Throwable) {
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

            override fun onResponse(
                call: Call<List<ConferenceList>>,
                response: Response<List<ConferenceList>>
            ) {
                if (response.code() == Constants.OK_RESPONSE) {
                    listener.onSuccess(response.body()!!)
                } else {
                    listener.onFailure(response.code())
                }
            }
        })
    }

    /**
     * ---------------------------------------------------------------------------------------------------------------------------
     */

    fun blockingStatus(mRoom: BlockRoom, token: String, listener: ResponseListener) {
        /**
         * API call using retrofit
         */
        var blockRoomApi = ServiceBuilder.getObject()
        val requestCall: Call<BlockingConfirmation> = blockRoomApi.blockConfirmation(token, mRoom)
        requestCall.enqueue(object : Callback<BlockingConfirmation> {
            override fun onFailure(call: Call<BlockingConfirmation>, t: Throwable) {
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

            override fun onResponse(call: Call<BlockingConfirmation>, response: Response<BlockingConfirmation>) {
                if (response.code() == Constants.OK_RESPONSE) {
                    if (response.body() == null) {
                        val blockingConfirmation = BlockingConfirmation()
                        blockingConfirmation.mStatus = 0
                        listener.onSuccess(blockingConfirmation)
                    } else {
                        val blockingConfirmation = response.body()
                        blockingConfirmation!!.mStatus = 1
                        listener.onSuccess(blockingConfirmation)
                    }
                }else {
                    listener.onFailure(response.code())
                }
            }
        })
    }
}
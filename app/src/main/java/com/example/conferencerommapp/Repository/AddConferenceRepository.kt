package com.example.conferencerommapp.Repository

import com.example.conferencerommapp.AddConferenceRoom
import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ResponseListener
import com.example.globofly.services.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddConferenceRepository {

    companion object {
        var mAddConferenceRepository: AddConferenceRepository? = null
        fun getInstance():AddConferenceRepository{
            if(mAddConferenceRepository == null){
                mAddConferenceRepository = AddConferenceRepository()
            }
            return mAddConferenceRepository!!
        }
    }

    //Passing the Context and model and call API, In return sends the status of LiveData
    fun addConferenceDetails(mConferenceRoom : AddConferenceRoom, token: String, listener: ResponseListener) {
        //Retrofit Call
        val addConferenceRoomService = ServiceBuilder.getObject()
        val addConferenceRequestCall: Call<ResponseBody> = addConferenceRoomService.addConference(token, mConferenceRoom)

        addConferenceRequestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onFailure(Constants.INTERNAL_SERVER_ERROR)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if ((response.code() == Constants.OK_RESPONSE) or (response.code() == Constants.SUCCESSFULLY_CREATED)) {
                    listener.onSuccess(response.code())
                }else {
                    listener.onFailure(response.code())
                }
            }
        })
    }
}
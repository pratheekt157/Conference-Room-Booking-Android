package com.example.conferencerommapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.Helper.ResponseListener
import com.example.conferencerommapp.Repository.HrConferenceRoomRepository
import com.example.myapplication.Models.ConferenceList

class HrConferenceRoomViewModel : ViewModel() {

    var mHrConferenceRoomRepository: HrConferenceRoomRepository? = null
    var mHrConferenceRoomList = MutableLiveData<List<ConferenceList>>()
    var mFailureCodeForHrConferenceRoom = MutableLiveData<Any>()

    fun getConferenceRoomList(buildingId: Int, token: String) {
        mHrConferenceRoomRepository = HrConferenceRoomRepository.getInstance()
        mHrConferenceRoomRepository!!.getConferenceRoomList(buildingId, token,  object : ResponseListener {
            override fun onSuccess(success: Any) {
                mHrConferenceRoomList.value = success as List<ConferenceList>
            }

            override fun onFailure(failure: Any) {
                mFailureCodeForHrConferenceRoom.value = failure
            }

        })
    }

    fun returnConferenceRoomList(): MutableLiveData<List<ConferenceList>> {
        return mHrConferenceRoomList
    }

    fun returnFailureForConferenceRoom(): MutableLiveData<Any> {
        return mFailureCodeForHrConferenceRoom
    }

}

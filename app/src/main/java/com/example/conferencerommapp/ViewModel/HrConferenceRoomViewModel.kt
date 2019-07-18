package com.example.conferencerommapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Repository.HrConferenceRoomRepository
import com.example.myapplication.Models.ConferenceList

class HrConferenceRoomViewModel : ViewModel() {

    var mHrConferenceRoomRepository: HrConferenceRoomRepository? = null
    var mHrConferenceRoomList = MutableLiveData<List<ConferenceList>>()
    var mFailureCodeForHrConferenceRoom = MutableLiveData<Any>()

    var mSuccessForDeleteBuilding = MutableLiveData<Int>()
    var mFailureForDeleteBuilding = MutableLiveData<Any>()

    fun getConferenceRoomList(buildingId: Int, token: String) {
        mHrConferenceRoomRepository = HrConferenceRoomRepository.getInstance()
        mHrConferenceRoomRepository!!.getConferenceRoomList(buildingId, token,  object :
            ResponseListener {
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

    fun deleteConferenceRoom(token: String,roomId: Int){
        mHrConferenceRoomRepository = HrConferenceRoomRepository.getInstance()
        mHrConferenceRoomRepository!!.deleteBuilding(token,roomId,object :ResponseListener{
            override fun onSuccess(success: Any) {
                mSuccessForDeleteBuilding.value = success as Int
            }

            override fun onFailure(failure: Any) {
                mFailureForDeleteBuilding.value = failure
            }

        })
    }
    fun returnSuccessForDeleteRoom(): MutableLiveData<Int> {
        return mSuccessForDeleteBuilding
    }
    /**
     * return negative response from server
     */
    fun returnFailureForDeleteRoom(): MutableLiveData<Any> {
        return mFailureForDeleteBuilding
    }
}

package com.example.conferencerommapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Model.ManagerConference
import com.example.conferencerommapp.Model.RoomDetails
import com.example.conferencerommapp.Repository.ManagerConferenceRoomRepository

class ManagerConferenceRoomViewModel : ViewModel() {

    /**
     * a object which will hold the reference to the corrosponding repository class
     */
    var mManagerConferenceRoomRepository: ManagerConferenceRoomRepository? = null

    /**
     * a MutableLivedata variable which will hold the Value for the Livedata
     */
    var mConferenceRoomList =  MutableLiveData<List<RoomDetails>>()
    var mFailureCode =  MutableLiveData<Any>()

    var mSuggestedConferenceRoomList = MutableLiveData<List<RoomDetails>>()
    var mSuggestedFailureCode = MutableLiveData<Any>()

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     */
    fun getConferenceRoomList(mRoom: ManagerConference, token: String) {
        mManagerConferenceRoomRepository = ManagerConferenceRoomRepository.getInstance()
        mManagerConferenceRoomRepository!!.getConferenceRoomList(
            mRoom,
            token,
            object : ResponseListener {
                override fun onSuccess(success: Any) {
                    mConferenceRoomList.value = success as List<RoomDetails>
                }

                override fun onFailure(failure: Any) {
                    mFailureCode.value = failure
                }

            }
        )
    }

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun getSuggestedConferenceRoomList(token: String, mInputDetailsForRoom: ManagerConference) {
        if (mManagerConferenceRoomRepository == null) {
            mManagerConferenceRoomRepository = ManagerConferenceRoomRepository.getInstance()
        }
        mManagerConferenceRoomRepository!!.getSuggestedRooms(token, mInputDetailsForRoom, object:
            ResponseListener {
            override fun onSuccess(success: Any) {
                mSuggestedConferenceRoomList.value = success as List<RoomDetails>
            }

            override fun onFailure(failure: Any) {
                mSuggestedFailureCode.value = failure
            }

        })
    }




    /**
     * function will return the MutableLiveData of List of buildings
     */
    fun returnSuccess(): MutableLiveData<List<RoomDetails>> {
        return mConferenceRoomList
    }

    /**
     * function will return the MutableLiveData of Int if something went wrong at server
     */
    fun returnFailure(): MutableLiveData<Any> {
        return mFailureCode
    }

    /**
     * function will return the MutableLiveData of List of buildings
     */
    fun returnSuccessForSuggested(): MutableLiveData<List<RoomDetails>> {
        return mSuggestedConferenceRoomList
    }

    /**
     * function will return the MutableLiveData of Int if something went wrong at server
     */
    fun returnFailureForSuggestedRooms(): MutableLiveData<Any> {
        return mSuggestedFailureCode
    }


}

package com.example.conferencerommapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.services.ResponseListener
import com.example.conferencerommapp.Model.BlockRoom
import com.example.conferencerommapp.Model.BlockingConfirmation
import com.example.conferencerommapp.Repository.BlockRoomRepository
import com.example.myapplication.Models.ConferenceList


class BlockRoomViewModel : ViewModel() {
    /**
     * a object which will hold the reference to the corresponding repository class
     */
    private var mBlockRoomRepository: BlockRoomRepository? = null

    /**
     * a MutableLiveData variable which will hold the positive response from server for the list of rooms
     */
    var mConferenceRoomList =  MutableLiveData<List<ConferenceList>>()

    /**
     * a MutableLiveData variable which will hold the for positive response from server for the confirmation of blocking
     */
    var mConfirmation =  MutableLiveData<BlockingConfirmation>()

    /**
     * a variable to hold positive response from backend for blocking room
     */
    var mSuccessForBlockRoom =  MutableLiveData<Int>()

    /**
     * a variable to hold failure code from backend whenever unable to fetch the list of room from server
     */
    var mFailureCodeForRoom = MutableLiveData<Any>()

    /**
     * a variable to hold failure code from backend whenever unable to fetch the confirmation details from server
     */
    var mFailureCodeForConfirmationOfBlocking = MutableLiveData<Any>()

    /**
     * a variable to hold failure code from backend whenever unable to block the room
     */
    var mFailureCodeForBlockRoom = MutableLiveData<Any>()

    fun setBlockRoomRepo(mBlockRoomRepo: BlockRoomRepository) {
        this.mBlockRoomRepository = mBlockRoomRepo
    }

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun blockRoom(mRoom: BlockRoom, token: String) {
        mBlockRoomRepository!!.blockRoom(mRoom, token,object: ResponseListener {
            override fun onSuccess(success: Any) {
                mSuccessForBlockRoom.value = success as Int
            }

            override fun onFailure(failure: Any) {
                mFailureCodeForBlockRoom.value = failure
            }

        })
    }

    fun returnSuccessForBlockRoom(): MutableLiveData<Int> {
        return mSuccessForBlockRoom
    }

    fun returnResponseErrorForBlockRoom():MutableLiveData<Any> {
        return mFailureCodeForBlockRoom
    }

    /**
     * ----------------------------------------------------------------------------------------------------------------
     */

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun getRoomList(buildingId: Int, token: String) {
        mBlockRoomRepository!!.getRoomList(buildingId, token, object:
            ResponseListener {
            override fun onSuccess(success: Any) {
                mConferenceRoomList.value = success as List<ConferenceList>
            }
            override fun onFailure(failure: Any) {
                mFailureCodeForRoom.value = failure
            }

        })
    }

    fun returnConferenceRoomList(): MutableLiveData<List<ConferenceList>> {
        return mConferenceRoomList
    }

    fun returnResponseErrorForConferenceRoom():MutableLiveData<Any> {
        return mFailureCodeForRoom
    }

    /**
     * ----------------------------------------------------------------------------------------------------------------
     */



    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun blockingStatus(mRoom: BlockRoom, token: String) {
        mBlockRoomRepository!!.blockingStatus(mRoom, token, object:
            ResponseListener {
            override fun onSuccess(success: Any) {
                mConfirmation.value = success as BlockingConfirmation
            }

            override fun onFailure(failure: Any) {
                mFailureCodeForConfirmationOfBlocking.value = failure
            }

        })
    }

    fun returnSuccessForConfirmation(): MutableLiveData<BlockingConfirmation> {
        return mConfirmation
    }

    fun returnResponseErrorForConfirmation():MutableLiveData<Any> {
        return mFailureCodeForConfirmationOfBlocking
    }



}
package com.example.conferencerommapp.addConferenceRoom.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.AddConferenceRoom
import com.example.conferencerommapp.addConferenceRoom.repository.AddConferenceRepository
import com.example.conferencerommapp.services.ResponseListener

class AddConferenceRoomViewModel : ViewModel() {
    /**
     * a object which will hold the reference to the corresponding repository class
     */
    var mAddConferenceRepository: AddConferenceRepository? = null
    /**
     * a MutableLivedata variable which will hold the Value for the Livedata
     */
    var mSuccessForAddingRoom = MutableLiveData<Int>()
    var mFailureForAddingRoom = MutableLiveData<Any>()


    var mSuccessForUpdateRoom = MutableLiveData<Int>()
    var mFailureForUpdateRoom = MutableLiveData<Any>()

    /**
     * initialize repo object
     */
    fun setAddingConferenceRoomRepo(mAddRoomRepo: AddConferenceRepository) {
        this.mAddConferenceRepository = mAddRoomRepo
    }

    /**
     * function will initialize the repository object and calls the method of repository which will make the api call
     * and function will return the value for MutableLivedata
     */
    fun addConferenceDetails(token: String, mAddConference: AddConferenceRoom) {
        mAddConferenceRepository!!.addConferenceDetails(mAddConference, token, object :
            ResponseListener {
            override fun onSuccess(success: Any) {
                mSuccessForAddingRoom.value = success as Int
            }

            override fun onFailure(failure: Any) {
                mFailureForAddingRoom.value = failure
            }
        })
    }

    fun returnSuccessForAddingRoom(): MutableLiveData<Int> {
        return mSuccessForAddingRoom
    }
    fun returnFailureForAddingRoom(): MutableLiveData<Any> {
        return mFailureForAddingRoom
    }

    //--------------------------------------------for update room details ------------------------------------------------

    fun updateConferenceDetails(token: String, mAddConference: AddConferenceRoom) {
        mAddConferenceRepository!!.updateConferenceDetails(mAddConference, token, object :
            ResponseListener {
            override fun onSuccess(success: Any) {
                mSuccessForUpdateRoom.value = success as Int
            }

            override fun onFailure(failure: Any) {
                mFailureForUpdateRoom.value = failure
            }
        })
    }

    fun returnSuccessForUpdateRoom(): MutableLiveData<Int> {
        return mSuccessForUpdateRoom
    }
    fun returnFailureForUpdateRoom(): MutableLiveData<Any> {
        return mFailureForUpdateRoom
    }

}
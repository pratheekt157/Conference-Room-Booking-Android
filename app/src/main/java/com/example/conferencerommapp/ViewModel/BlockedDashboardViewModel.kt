package com.example.conferencerommapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conferencerommapp.Blocked
import com.example.conferencerommapp.Helper.ResponseListener
import com.example.conferencerommapp.Helper.Unblock
import com.example.conferencerommapp.Repository.BlockDashboardRepository

class BlockedDashboardViewModel : ViewModel() {

    var mBlockDashboardRepository: BlockDashboardRepository? = null
    var mBlockedRoomList = MutableLiveData<List<Blocked>>()
    var mFailureCodeForBlockedRoomList = MutableLiveData<Any>()
    var mSuccessCodeForBlockRoom =  MutableLiveData<Int>()
    var mFailureCodeForBlockRoom =  MutableLiveData<Any>()

    fun getBlockedList(token: String) {
        mBlockDashboardRepository = BlockDashboardRepository.getInstance()
        mBlockDashboardRepository!!.getBlockedList(token, object : ResponseListener {
            override fun onSuccess(success: Any) {
                mBlockedRoomList.value = success as List<Blocked>
            }

            override fun onFailure(failure: Any) {
                mFailureCodeForBlockedRoomList.value = failure
            }

        })

    }
    fun returnBlockedRoomList(): MutableLiveData<List<Blocked>> {
        return mBlockedRoomList
    }

    fun returnFailureCodeFromBlockedApi(): MutableLiveData<Any> {
        return mFailureCodeForBlockedRoomList
    }
    //-----------------------------------------------------------------------------------------------------------

    fun unBlockRoom(token: String, bookingId: Int) {
        mBlockDashboardRepository = BlockDashboardRepository.getInstance()
        mBlockDashboardRepository!!.unblockRoom(token, bookingId, object: ResponseListener {
            override fun onSuccess(success: Any) {
                mSuccessCodeForBlockRoom.value = success as Int
            }

            override fun onFailure(failure: Any) {
                mFailureCodeForBlockRoom.value = failure
            }

        })
    }

    fun returnSuccessCodeForUnBlockRoom(): MutableLiveData<Int> {
        return mSuccessCodeForBlockRoom
    }
    fun returnFailureCodeForUnBlockRoom(): MutableLiveData<Any> {
        return mFailureCodeForBlockRoom
    }
}
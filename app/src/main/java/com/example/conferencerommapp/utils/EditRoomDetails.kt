package com.example.conferencerommapp.utils

import com.example.myapplication.Models.ConferenceList
import java.io.Serializable

data class EditRoomDetails (
    var mRoomDetail: ConferenceList? = null
): Serializable
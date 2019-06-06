package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class UpdateBooking(

    @SerializedName("NewStartTime")
    var newFromTime: String? = null,

    @SerializedName("NewEndTime")
    var newtotime: String? =null,

    @SerializedName("MeetId")
    var bookingId: Int? = null
)
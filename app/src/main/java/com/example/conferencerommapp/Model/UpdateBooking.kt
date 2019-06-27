package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class UpdateBooking(

    @SerializedName("newStartTime")
    var newFromTime: String? = null,

    @SerializedName("newEndTime")
    var newtotime: String? =null,

    @SerializedName("meetId")
    var bookingId: Int? = null
)
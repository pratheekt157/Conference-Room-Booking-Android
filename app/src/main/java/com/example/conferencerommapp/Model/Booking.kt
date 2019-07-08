package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class Booking (

    @SerializedName("emailId")
    var email: String? = null,

    @SerializedName("roomId")
    var roomId: Int? = 0,

    @SerializedName("buildingId")
    var buildingId: Int? = 0,

    @SerializedName("startTime")
    var fromTime: String? = null,

    @SerializedName("endTime")
    var toTime: String? = null,

    @SerializedName("purpose")
    var purpose: String? = null,

    @SerializedName("attendeesMail")
    var cCMail: String? = null,

    @SerializedName("isPurposeVisible")
    var isPurposeVisible: Boolean? = null
)

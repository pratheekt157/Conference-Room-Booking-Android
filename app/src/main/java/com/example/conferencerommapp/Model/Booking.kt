package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class Booking (

    @SerializedName("EmailId")
    var email: String? = null,

    @SerializedName("RoomId")
    var roomId: Int? = 0,

    @SerializedName("BuildingId")
    var buildingId: Int? = 0,

    @SerializedName("StartTime")
    var fromTime: String? = null,

    @SerializedName("EndTime")
    var toTime: String? = null,

    @SerializedName("Purpose")
    var purpose: String? = null,

    @SerializedName("AttendeesMail")
    var cCMail: String? = null
)

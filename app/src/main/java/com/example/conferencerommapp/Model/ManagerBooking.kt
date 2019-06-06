package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class ManagerBooking(

    @SerializedName("EmailId")
    var email: String? = null,

    @SerializedName("RoomId")
    var roomId: Int? = 0,

    @SerializedName("BuildingId")
    var buildingId: Int? = 0,

    @SerializedName("StartTime")
    var fromTime: ArrayList<String>? = null,

    @SerializedName("RoomName")
    var roomName: String? = null,

    @SerializedName("EndTime")
    var toTime: ArrayList<String>? = null,

    @SerializedName("Purpose")
    var purpose: String? = null,

    @SerializedName("Capacity")
    var capacity: Int? = 0,

    @SerializedName("Attendee")
    var cCMail: String? = null

)
package com.example.conferencerommapp

import com.google.gson.annotations.SerializedName

data class Blocked(

    @SerializedName("RoomId")
    val roomId : Int? =0,

    @SerializedName("BuildingName")
    val buildingName : String? = null,

    @SerializedName("RoomName")
    val roomName : String? = null,

    @SerializedName("StartTime")
    val fromTime: String? = null,

    @SerializedName("EndTime")
    val toTime: String? = null,

    @SerializedName("Purpose")
    val purpose: String? = null,

    @SerializedName("MeetId")
    val bookingId: Int? = null
)
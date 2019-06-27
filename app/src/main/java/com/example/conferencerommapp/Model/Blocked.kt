package com.example.conferencerommapp

import com.google.gson.annotations.SerializedName

data class Blocked(

    @SerializedName("roomId")
    val roomId : Int? =0,

    @SerializedName("buildingName")
    val buildingName : String? = null,

    @SerializedName("roomName")
    val roomName : String? = null,

    @SerializedName("startTime")
    val fromTime: String? = null,

    @SerializedName("endTime")
    val toTime: String? = null,

    @SerializedName("purpose")
    val purpose: String? = null,

    @SerializedName("meetId")
    val bookingId: Int? = null
)
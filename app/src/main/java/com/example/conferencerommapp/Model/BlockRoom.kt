package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class BlockRoom(
    @SerializedName("RoomId")
    var cId: Int? = 0,

    @SerializedName("EmailId")
    var email: String? = null,

    @SerializedName("StartTime")
    var fromTime: String? = null,

    @SerializedName("EndTime")
    var toTime: String? = null,

    @SerializedName("BuildingId")
    var bId: Int? = 0,

    @SerializedName("Status")
    var status: String? = null,

    @SerializedName("Purpose")
    var purpose: String? = null
)
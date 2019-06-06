package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class RoomDetails(

    @SerializedName("RoomName")
    var roomName: String? = null,

    @SerializedName("BuildingName")
    var buildingName: String? = null,

    @SerializedName("Place")
    var place: String? = null,

    @SerializedName("RoomId")
    var roomId: Int? = null,

    @SerializedName("BuildingId")
    var buildingId: Int? = null,

    @SerializedName("Status")
    var status: String? = null,

    @SerializedName("Capacity")
    var capacity: Int? = null,

    @SerializedName("Amenities")
    var amenities: List<String>? = null,

    @SerializedName("Permission")
    var permission: Int? = 0
)
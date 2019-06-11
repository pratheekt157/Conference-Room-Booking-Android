package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName
import java.util.*

data class InputDetailsForRoom (
    @SerializedName("StartTime")
    var fromTime : String? = null,

    @SerializedName("EndTime")
    var toTime : String? = null,

    @SerializedName("BuildingId")
    var buildingId : Int? = 0,

    @SerializedName("Capacity")
    var capacity : Int? = 0,

    @SerializedName("EmailId")
    var email : String? = null



)
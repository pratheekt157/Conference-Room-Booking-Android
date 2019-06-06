package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

class ManagerConference {
    @SerializedName("StartTime")
    var fromTime = ArrayList<String>()

    @SerializedName("EndTime")
    var toTime = ArrayList<String>()

    @SerializedName("BuildingId")
    var buildingId : Int? = 0

    @SerializedName("Capacity")
    var capacity: Int? = 0
}
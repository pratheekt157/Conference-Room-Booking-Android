package com.example.conferencerommapp.Model
import com.google.gson.annotations.SerializedName

data class Building (
    @SerializedName("BuildingId")
    var buildingId: String? = null,

    @SerializedName("BuildingName")
    var buildingName: String? = null,

    @SerializedName("Place")
    var buildingPlace: String? = null
)


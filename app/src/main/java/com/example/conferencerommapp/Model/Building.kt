package com.example.conferencerommapp.Model
import com.google.gson.annotations.SerializedName

data class Building (
    @SerializedName("buildingId")
    var buildingId: String? = null,

    @SerializedName("buildingName")
    var buildingName: String? = null,

    @SerializedName("place")
    var buildingPlace: String? = null
)


package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class BlockingConfirmation (
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("Purpose")
    val purpose: String? = null,
    var mStatus: Int? = 0
)
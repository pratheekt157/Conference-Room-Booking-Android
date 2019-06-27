package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class BlockingConfirmation (
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("purpose")
    val purpose: String? = null,
    var mStatus: Int? = 0
)
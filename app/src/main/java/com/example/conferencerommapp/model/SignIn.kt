package com.example.conferencerommapp.model

import com.google.gson.annotations.SerializedName

data class SignIn(

        @SerializedName("token")
        var token: String? = null,

        @SerializedName("statusCode")
        var statusCode: String? = null
)
package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class SignIn(
        @SerializedName("token")
        var Token:String?= null,
        @SerializedName("statusCode")
        var StatusCode:String? = null
)
package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class Employee (


    @SerializedName("Email")
    var email: String? = null,

    @SerializedName("Name")
    var name: String? = null,

    @SerializedName("UserId")
    var userId: String? = null

)
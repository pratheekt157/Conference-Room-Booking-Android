package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class Employee (


    @SerializedName("email")
    var email: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("userId")
    var userId: String? = null

)
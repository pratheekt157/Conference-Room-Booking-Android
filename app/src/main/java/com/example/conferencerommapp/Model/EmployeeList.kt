package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName

data class EmployeeList (

    @SerializedName("EmailId")
    var email: String? = null,

    @SerializedName("Name")
    var name: String? = null
)
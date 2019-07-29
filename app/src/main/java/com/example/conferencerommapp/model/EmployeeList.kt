package com.example.conferencerommapp.model

import com.google.gson.annotations.SerializedName

data class EmployeeList (

    @SerializedName("emailId")
    var email: String? = null,

    @SerializedName("name")
    var name: String? = null
)
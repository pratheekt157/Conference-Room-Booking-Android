package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.*

data class BookingDashboardInput (
    @SerializedName("EmailId")
    var email: String? = null,

    @SerializedName("Status")
    var status: String? = null,

    @SerializedName("PageSize")
    var pageSize: Int? = null,

    @SerializedName("PageNumber")
    var pageNumber: Int? = null,

    @SerializedName("CurrentDateTime")
    var currentDatTime: String? = null


    )
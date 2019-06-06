package com.example.conferencerommapp.Model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Dashboard (
    @SerializedName("RoomId")
    var roomId : Int? = null,

    @SerializedName("EmailId")
    var email: String? = null,

    @SerializedName("StartTime")
    var fromTime : String? = null,

    @SerializedName("EndTime")
    var toTime : String? = null,

    @SerializedName("BuildingName")
    var buildingName : String? = null,

    @SerializedName("RoomName")
    var roomName : String? = null,

    @SerializedName("Purpose")
    var purpose: String? = null,

    @SerializedName("Status")
    var status: String? = null,

    @SerializedName("MeetId")
    var bookingId: Int? = null,

    @SerializedName("NameOfAttendees")
    var name: List<String>? = null,

    @SerializedName("AttendeesMail")
    var cCMail: List<String>? = null,

    @SerializedName("Amenities")
    var amenities: List<String>? = null,

    @SerializedName("NameOfOrganizer")
    var organizer: String? = null,

    @SerializedName("IsTagged")
    var isTagged: Boolean? = false
)

data class PaginationMetaData(
    @SerializedName("TotalCount")
    var pageCount: Int? = null,

    @SerializedName("PageSize")
    var pageSize: Int? = null,

    @SerializedName("CurrentPage")
    var currentPage: Int? = null,

    @SerializedName("TotalPages")
    var totalPages: Int? = null,

    @SerializedName("PreviousPage")
    var previousPage: Boolean? = null,

    @SerializedName("NextPage")
    var nextPage: Boolean? = null
)

data class DashboardDetails(
    @SerializedName("UserDashboard")
    var dashboard: List<Dashboard>? = null,

    @SerializedName("Pager")
    var paginationMetaData: PaginationMetaData? = null
)


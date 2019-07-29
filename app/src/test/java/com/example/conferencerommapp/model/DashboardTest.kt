package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DashboardTest{
    val objectMapper = jacksonObjectMapper()
    val dashboard = Dashboard()
    val paginationMetaData =PaginationMetaData()
    val dashBoardDetails = DashboardDetails()
    @Test
    fun dashboard(){
        dashboard.roomId = 0
        dashboard.email = ""
        dashboard.fromTime = ""
        dashboard.toTime =  ""
        dashboard.buildingName = ""
        dashboard.roomName = ""
        dashboard.purpose = ""
        dashboard.status = ""
        dashboard.bookingId = 1
        dashboard.name = null
        dashboard.cCMail =null
        val data= objectMapper.writeValueAsString(dashboard)
        assertEquals(data,"{\"roomId\":0,\"email\":\"\",\"fromTime\":\"\",\"toTime\":\"\",\"buildingName\":\"\",\"roomName\":\"\",\"purpose\":\"\",\"status\":\"\",\"bookingId\":1,\"name\":null,\"amenities\":null,\"organizer\":null,\"recurringmeetingId\":null,\"ccmail\":null,\"tagged\":false}")
    }

    @Test
    fun paginationMetaData(){
        paginationMetaData.pageCount =0
        paginationMetaData.pageSize = 0
        paginationMetaData.currentPage = 0
        paginationMetaData.totalPages = 0
        paginationMetaData.previousPage = false
        paginationMetaData.nextPage = false

        val paginationData = objectMapper.writeValueAsString(paginationMetaData)
        assertEquals(paginationData,"{\"pageCount\":0,\"pageSize\":0,\"currentPage\":0,\"totalPages\":0,\"previousPage\":false,\"nextPage\":false}")
    }

    @Test
    fun dashBoardDetails(){
        dashBoardDetails.dashboard = null
        dashBoardDetails.paginationMetaData = null
        val dashboardDetailsData  = objectMapper.writeValueAsString(dashBoardDetails)
        assertEquals(dashboardDetailsData,"{\"dashboard\":null,\"paginationMetaData\":null}")
    }
}
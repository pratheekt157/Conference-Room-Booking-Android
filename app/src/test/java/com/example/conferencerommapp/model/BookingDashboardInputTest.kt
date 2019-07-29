package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookingDashboardInputTest{
    val objectMapper = jacksonObjectMapper()
    val bookingDashboard= BookingDashboardInput()

    @Test
    fun bookingDashboard(){
        bookingDashboard.email = ""
        bookingDashboard.status = ""
        bookingDashboard.pageSize = 1
        bookingDashboard.pageNumber = 0
        bookingDashboard.currentDatTime = ""
        val data = objectMapper.writeValueAsString(bookingDashboard)
        assertEquals(data,"{\"email\":\"\",\"status\":\"\",\"pageSize\":1,\"pageNumber\":0,\"currentDatTime\":\"\"}")
    }
}
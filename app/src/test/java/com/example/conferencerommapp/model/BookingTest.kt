package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookingTest{
    val objectMapper = jacksonObjectMapper()
    val booking = Booking()

    @Test
    fun booking(){
        booking.email = ""
        booking.isPurposeVisible = true
        booking.purpose = ""
        booking.buildingId = 0
        booking.cCMail = ""
        booking.fromTime = ""
        booking.toTime = ""
        booking.roomId = 0
        val data = objectMapper.writeValueAsString(booking)
//        assertEquals(data,"")
     //   assertEquals(data,"{\"email\":\"\",\"roomId\":0,\"buildingId\":0,\"fromTime\":\"\",\"toTime\":\"\",\"purpose\":\"\",\"purposeVisible\":true,\"ccmail\":\"\"}")
        assertEquals(data,"{\"email\":\"\",\"roomId\":0,\"buildingId\":0,\"fromTime\":\"\",\"toTime\":\"\",\"purpose\":\"\",\"purposeVisible\":true,\"ccmail\":\"\"}")
    }
}
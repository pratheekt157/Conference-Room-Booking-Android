package com.example.conferencerommapp.Model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ManagerBookingTest{
    val objectMapper = jacksonObjectMapper()
    val mManagerBooking  =ManagerBooking()

    @Test
    fun managerBookingTes(){
        mManagerBooking.buildingId = 0
        mManagerBooking.cCMail = ""
        mManagerBooking.capacity = 0
        mManagerBooking.email= ""
        mManagerBooking.fromTime = null
        mManagerBooking.purpose = ""
        mManagerBooking.roomId = 0
        mManagerBooking.roomName =""
        mManagerBooking.toTime = null

        val data = objectMapper.writeValueAsString(mManagerBooking)
        assertEquals(data,"{\"email\":\"\",\"roomId\":0,\"buildingId\":0,\"fromTime\":null,\"roomName\":\"\",\"toTime\":null,\"purpose\":\"\",\"capacity\":0,\"ccmail\":\"\"}")
    }
}
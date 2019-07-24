package com.example.conferencerommapp.Model

import com.example.conferencerommapp.Blocked
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BlockedTest{
    val objectMapper = jacksonObjectMapper()
    val blocked = Blocked()

    @Test
    fun blocked(){
        blocked.roomId = 1
        blocked.buildingName = ""
        blocked.roomName = ""
        blocked.fromTime = ""
        blocked.toTime = ""
        blocked.purpose = ""
        blocked.bookingId = 1
        val data = objectMapper.writeValueAsString(blocked)
        assertEquals(data,"{\"roomId\":1,\"buildingName\":\"\",\"roomName\":\"\",\"fromTime\":\"\",\"toTime\":\"\",\"purpose\":\"\",\"bookingId\":1}")
    }
}
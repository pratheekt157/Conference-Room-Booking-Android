package com.example.conferencerommapp.Model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InputDetailsForRoomTest{
    val objectMapper = jacksonObjectMapper()
    val inputDetailsForRoomTest = InputDetailsForRoom()

    @Test
    fun inputDetailsForRoom(){
        inputDetailsForRoomTest.buildingId =1
        inputDetailsForRoomTest.capacity =0
        inputDetailsForRoomTest.email =""
        inputDetailsForRoomTest.fromTime =""
        inputDetailsForRoomTest.toTime = ""

        val data = objectMapper.writeValueAsString(inputDetailsForRoomTest)
        assertEquals(data,"{\"fromTime\":\"\",\"toTime\":\"\",\"buildingId\":1,\"capacity\":0,\"email\":\"\"}")
    }
}
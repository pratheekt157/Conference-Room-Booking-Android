package com.example.conferencerommapp.Model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BlockRoomTest{
    val objectMapper = jacksonObjectMapper()
    val blockRoom = BlockRoom()

    @Test
    fun blockRoom(){
        blockRoom.cId =0
        blockRoom.bId =1
        blockRoom.email = ""
        blockRoom.fromTime = ""
        blockRoom.purpose = ""
        blockRoom.status = ""
        blockRoom.toTime = " "
        val data = jacksonObjectMapper().writeValueAsString(blockRoom)
        //assertEquals(data,"")
        assertEquals(data,"{\"email\":\"\",\"fromTime\":\"\",\"toTime\":\" \",\"status\":\"\",\"purpose\":\"\",\"cid\":0,\"bid\":1}")

    }
}
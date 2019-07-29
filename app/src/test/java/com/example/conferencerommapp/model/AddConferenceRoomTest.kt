package com.example.conferencerommapp.model

import com.example.conferencerommapp.AddConferenceRoom
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddConferenceRoomTest{
    val objectMapper = jacksonObjectMapper()
    val addConference = AddConferenceRoom()

    @Test
    fun addConferenceRoom(){
        addConference.bId = 0
        addConference.capacity =4
        addConference.roomId = 1
        addConference.newRoomName =""
        addConference.roomName = ""
        addConference.extensionBoard = true
        addConference.monitor = true
        addConference.projector = true
        addConference.whiteBoardMarker = true
        addConference.permission = true
        addConference.speaker =true

        val data= objectMapper.writeValueAsString(addConference)
        //assertEquals(data,"")
        assertEquals(data,"{\"roomId\":1,\"newRoomName\":\"\",\"roomName\":\"\",\"capacity\":4,\"projector\":true,\"monitor\":true,\"speaker\":true,\"extensionBoard\":true,\"whiteBoardMarker\":true,\"permission\":true,\"bid\":0}")

    }
}
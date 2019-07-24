package com.example.conferencerommapp.Model

import com.example.myapplication.Models.ConferenceList
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConferenceListTest{
    val objectMapper = jacksonObjectMapper()
    val conferenceList = ConferenceList()


    @Test
    fun confereceList(){
        conferenceList.roomName = ""
        conferenceList.capacity = 0
        conferenceList.buildingName = ""
        conferenceList.roomId = 0
        conferenceList.buildingId =1
        conferenceList.amenities = null
        conferenceList.place = ""
        conferenceList.permission = 0

        val data = objectMapper.writeValueAsString(conferenceList)
        //assertEquals(data,"")
        assertEquals(data,"{\"roomName\":\"\",\"capacity\":0,\"buildingName\":\"\",\"roomId\":0,\"buildingId\":1,\"amenities\":null,\"place\":\"\",\"permission\":0}")

    }
}
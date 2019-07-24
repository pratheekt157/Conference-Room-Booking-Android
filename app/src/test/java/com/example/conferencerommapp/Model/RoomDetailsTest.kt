package com.example.conferencerommapp.Model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RoomDetailsTest{
    val objectMapper = jacksonObjectMapper()
    val mRoomDetails = RoomDetails()

    @Test
    fun roomDetails(){
        mRoomDetails.roomName = ""
        mRoomDetails.buildingName =""
        mRoomDetails.place = ""
        mRoomDetails.roomId = 2
        mRoomDetails.buildingId =2
        mRoomDetails.status = ""
        mRoomDetails.capacity = 0
        mRoomDetails.amenities = null
        mRoomDetails.permission =0

        val data = objectMapper.writeValueAsString(mRoomDetails)
        assertEquals(data,"{\"roomName\":\"\",\"buildingName\":\"\",\"place\":\"\",\"roomId\":2,\"buildingId\":2,\"status\":\"\",\"capacity\":0,\"amenities\":null,\"permission\":0}")
    }
}
package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ManagerConferenceTest{
    val objectMapper = jacksonObjectMapper()
    val mManagerConference = ManagerConference()

    @Test
    fun managerConferenceTest(){
        mManagerConference.buildingId =0
        mManagerConference.capacity = 0
        mManagerConference.fromTime= ArrayList()
        mManagerConference.toTime = ArrayList()

        val data = objectMapper.writeValueAsString(mManagerConference)
        assertEquals(data,"{\"fromTime\":[],\"toTime\":[],\"buildingId\":0,\"capacity\":0}")
    }
}
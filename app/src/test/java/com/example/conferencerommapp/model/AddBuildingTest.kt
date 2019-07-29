package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddBuildingTest{
    val objectMapper = jacksonObjectMapper()
    val addBuilding = AddBuilding()

    @Test
    fun addBuilding(){
        addBuilding.buildingId = 1
        addBuilding.buildingName = ""
        addBuilding.place = ""
        val data = objectMapper.writeValueAsString(addBuilding)
        assertEquals(data,"{\"buildingName\":\"\",\"place\":\"\",\"buildingId\":1}")

    }
}
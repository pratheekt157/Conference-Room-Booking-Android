package com.example.conferencerommapp.Model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BuildingTest{
    val objectMapper = jacksonObjectMapper()
    val building = Building()

    @Test
    fun building(){
        building.buildingId = ""
        building.buildingName = ""
        building.buildingPlace = ""
        val data = objectMapper.writeValueAsString(building)
       // assertEquals(data,"")
        assertEquals(data,"{\"buildingId\":\"\",\"buildingName\":\"\",\"buildingPlace\":\"\"}")
    }
}
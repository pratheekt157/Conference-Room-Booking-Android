package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BlockingConfirmationTest{
    val objectMapper = jacksonObjectMapper()
    val blockingConfirmation  =BlockingConfirmation()

    @Test
    fun blockConfirmation(){
        blockingConfirmation.name = ""
        blockingConfirmation.mStatus = 1
        blockingConfirmation.purpose = ""
        val data = objectMapper.writeValueAsString(blockingConfirmation)
        //assertEquals(data,"")
        assertEquals(data,"{\"name\":\"\",\"purpose\":\"\",\"mstatus\":1}")
    }
}
package com.example.conferencerommapp.Model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RefreshTokenTest{

    val objectMapper = jacksonObjectMapper()
    val mRefreshToken = RefreshToken()

    @Test
    fun refreshTokenTest(){
        mRefreshToken.accessToken= ""
        mRefreshToken.refreshToken = ""

        val data = objectMapper.writeValueAsString(mRefreshToken)
        assertEquals(data,"{\"accessToken\":\"\",\"refreshToken\":\"\"}")
    }
}
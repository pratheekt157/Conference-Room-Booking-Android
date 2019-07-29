package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignInTest{
    val objectMapper= jacksonObjectMapper()
    val mSignIn = SignIn()

    @Test
    fun signIn(){
        mSignIn.statusCode = ""
        mSignIn.token = ""
        val data = objectMapper.writeValueAsString(mSignIn)
        assertEquals(data,"{\"statusCode\":\"\",\"token\":\"\"}")
    }


}
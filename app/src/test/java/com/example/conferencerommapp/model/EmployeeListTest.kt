package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EmployeeListTest{
    val objectMapper = jacksonObjectMapper()
    val employeeList = EmployeeList()

    @Test
    fun employeeList(){
        employeeList.email = ""
        employeeList.name = ""
        val data =  objectMapper.writeValueAsString(employeeList)
        assertEquals(data,"{\"email\":\"\",\"name\":\"\"}")
    }
}
package com.example.conferencerommapp.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EmployeeTest{
    val objectMapper = jacksonObjectMapper()
    val employee = Employee()

    @Test
    fun employeeTest(){
        employee.email= ""
        employee.name = ""
        employee.userId = ""
        val data = objectMapper.writeValueAsString(employee)
        assertEquals(data,"{\"email\":\"\",\"name\":\"\",\"userId\":\"\"}")
    }
}
package com.example.conferencerommapp.Model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateBookingTest{
    val objectMapper = jacksonObjectMapper()
    val mUpdateBooking = UpdateBooking()

    @Test
    fun updateBooking()
    {
        mUpdateBooking.bookingId = 1
        mUpdateBooking.newFromTime= ""
        mUpdateBooking.newtotime =""
        val data = objectMapper.writeValueAsString(mUpdateBooking)
        assertEquals(data,"{\"newFromTime\":\"\",\"newtotime\":\"\",\"bookingId\":1}")
    }
}
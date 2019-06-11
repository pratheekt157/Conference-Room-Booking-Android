package com.example.conferencerommapp.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.conferencerommapp.R
import java.text.SimpleDateFormat
import java.util.*


class MainActivityTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_test)
        formatDateAsUTC()
    }
    @SuppressLint("SimpleDateFormat")
    fun formatDateAsUTC() {

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

        var d = simpleDateFormat.parse("2019-06-10 19:25")

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'")

        sdf.timeZone = TimeZone.getTimeZone("UTC")

        Log.i("Time----------" , sdf.format(d))


        var pstFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        pstFormat.timeZone = TimeZone.getTimeZone(getCurrentTimeZone())

        getTimeInIndianStandardTime("2019-06-11 12:30:00")
    }
    fun getCurrentTimeZone(): String {
        return Calendar.getInstance().timeZone.id
    }

    fun getTimeInIndianStandardTime(time: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        var pstFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        pstFormat.timeZone = TimeZone.getTimeZone(getCurrentTimeZone())
        pstFormat.format(sdf.parse(time))
        Log.i("Time In Local--------" , pstFormat.format(sdf.parse(time)))
    }
}

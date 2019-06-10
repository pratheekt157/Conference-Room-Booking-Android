package com.example.conferencerommapp.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.conferencerommapp.Helper.RoomAdapterNew
import com.example.conferencerommapp.R
import kotlinx.android.synthetic.main.check_recycler_view.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS





class CheckAdapter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_recycler_view)
        beepForAnHour()
    }

    fun beepForAnHour() {
        val scheduler = Executors.newScheduledThreadPool(1)
        val beeper = Runnable {
            Log.i("--------------", "beep")
        }
        scheduler.scheduleAtFixedRate(beeper, 0, 60, TimeUnit.SECONDS)
//        scheduler.schedule({
//            beeperHandle.cancel(true)
//        }, 60 * 60, TimeUnit.SECONDS)
    }



    private fun setAdapter(mListOfRooms: List<String>) {

        var customAdapter =
            RoomAdapterNew(mListOfRooms as ArrayList<String>, this)
        recycler_view_room_list1.adapter = customAdapter
        booking_scroll_view.smoothScrollTo(0,recycler_view_room_list1.top)
        booking_scroll_view.post(object :Runnable{
            override fun run() {
                suggestions.visibility = View.VISIBLE
                booking_scroll_view.smoothScrollTo(0,recycler_view_room_list1.top)
            }

        })
    }
}
package com.example.conferencerommapp.Activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.conferencerommapp.Helper.RoomAdapter
import com.example.conferencerommapp.Helper.RoomAdapterNew
import com.example.conferencerommapp.Model.RoomDetails
import com.example.conferencerommapp.R
import kotlinx.android.synthetic.main.check_recycler_view.*
import kotlinx.android.synthetic.main.check_recycler_view.view.*

class CheckAdapter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_recycler_view)
        search_room.setOnClickListener {
            var list = mutableListOf<String>("A","B", "C", "D")
                //, "B", "C", "D", "E", "F", "G", "H")
            setAdapter(list)
        }
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
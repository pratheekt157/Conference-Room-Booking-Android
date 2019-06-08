package com.example.conferencerommapp.Helper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.conferencerommapp.Model.RoomDetails
import com.example.conferencerommapp.R
import org.w3c.dom.Text

@Suppress("NAME_SHADOWING")
class RoomAdapterNew(
    private val roomDetailsList: ArrayList<String>,
    val mContext: Context
) : androidx.recyclerview.widget.RecyclerView.Adapter<RoomAdapterNew.ViewHolder>() {

    /**
     * this override function will set a view for the recyclerview items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conference_room_list, parent, false)
        return ViewHolder(view)
    }

    /**
     * bind data with the view
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.buildingNameTextView.text = "ABC"


    }



    /**
     * it will return number of items contains in recyclerview view
     */
    override fun getItemCount(): Int {
        return roomDetailsList.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var buildingNameTextView: TextView = itemView.findViewById(R.id.building_name)
        var roomNameTextView: TextView = itemView.findViewById(R.id.room_name)
        var roomDetails: RoomDetails? = null
    }




}

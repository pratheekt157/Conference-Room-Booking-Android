package com.example.conferencerommapp.Helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.conferencerommapp.R

import com.example.myapplication.Models.ConferenceList

class ConferenceRecyclerAdapter(private val conferencceList: List<ConferenceList>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ConferenceRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conference_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return conferencceList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.conferencelist = conferencceList[position]
        holder.buildingName.text = conferencceList[position].buildingName
        holder.conferenceName.text = conferencceList[position].roomName
        holder.conferenceCapacity.text = conferencceList[position].capacity.toString()
        if(conferencceList[position].amenities!!.isEmpty()) {
            holder.resourceDetail.visibility = View.VISIBLE
        } else {
            holder.resourceDetail.visibility = View.GONE
            for(i in conferencceList[position].amenities!!.indices) {
                when(i) {
                    0 -> {
                        setDrawable(conferencceList[position].amenities!![i], holder.amenity1)
                        holder.amenity1.visibility = View.VISIBLE

                    }
                    1-> {
                        setDrawable(conferencceList[position].amenities!![i], holder.amenity2)
                        holder.amenity2.visibility = View.VISIBLE
                    }
                    2-> {
                        setDrawable(conferencceList[position].amenities!![i], holder.amenity3)
                        holder.amenity3.visibility = View.VISIBLE
                    }
                    3-> {
                        setDrawable(conferencceList[position].amenities!![i], holder.amenity4)
                        holder.amenity4.visibility = View.VISIBLE
                    }
                    4 -> {
                        setDrawable(conferencceList[position].amenities!![i], holder.amenity5)
                        holder.amenity5.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val conferenceName: TextView = itemView.findViewById(R.id.room_name_show)
        val conferenceCapacity: TextView = itemView.findViewById(R.id.conference_room_capacity_show)
        val buildingName: TextView = itemView.findViewById(R.id.dashboard_building_name)
        var resourceDetail: TextView = itemView.findViewById(R.id.dashboard_no_resource_detail_found)
        var amenity1: TextView = itemView.findViewById(R.id.dashboard_ani_1)
        var amenity2: TextView = itemView.findViewById(R.id.dashboard_ani_2)
        var amenity3: TextView = itemView.findViewById(R.id.dashboard_ani_3)
        var amenity4: TextView = itemView.findViewById(R.id.dashboard_ani_4)
        var amenity5: TextView = itemView.findViewById(R.id.dashboard_ani_5)
        var conferencelist: ConferenceList? = null
    }
    fun setDrawable(amitie: String, targetTextView: TextView) {
        when(amitie) {
            "Projector" -> {
                targetTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_projector, 0, 0, 0)
            }
            "WhiteBoard-Marker" -> {
                targetTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_white_board2, 0, 0, 0)
            }
            "Monitor" -> {
                targetTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_tv_black_24dp, 0, 0, 0)
            }
            "Speaker" -> {
                targetTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_speaker, 0, 0, 0)
            }
            "Extension Board" -> {
                targetTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_extension_board, 0, 0, 0)
            }
        }
        targetTextView.text = amitie
    }

}
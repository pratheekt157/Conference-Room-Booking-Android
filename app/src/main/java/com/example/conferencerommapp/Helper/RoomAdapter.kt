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
class RoomAdapter(
    private val roomDetailsList: ArrayList<RoomDetails>,
    val mContext: Context,
    val listener: RoomAdapter.ItemClickListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

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

        var amiList = roomDetailsList[position].amenities
        if(roomDetailsList[position].permission == 1) {
            holder.permissionTextView.visibility = View.VISIBLE
        }
        for (i in amiList!!.indices) {
            when (i) {
                0 -> {
                    setDrawable(roomDetailsList[position].amenities!![i], holder.amenity1)
                    holder.amenity1.visibility = View.VISIBLE
                }
                1 -> {
                    setDrawable(roomDetailsList[position].amenities!![i], holder.amenity2)
                    holder.amenity2.visibility = View.VISIBLE
                }
                2 -> {
                    setDrawable(roomDetailsList[position].amenities!![i], holder.amenity3)
                    holder.amenity3.visibility = View.VISIBLE
                }
                3 -> {
                    setDrawable(roomDetailsList[position].amenities!![i], holder.amenity4)
                    holder.amenity4.visibility = View.VISIBLE
                }
                4 -> {
                    setDrawable(roomDetailsList[position].amenities!![i], holder.amenity5)
                    holder.amenity5.visibility = View.VISIBLE
                }
            }
        }
        if (roomDetailsList[position].status == "Unavailable") {
            holder.statusTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_status_unavailable, 0, 0)
            holder.statusTextView.textSize = 11F
            holder.statusTextView.text = "Unavailable"
        }
//        else {
//            holder.statusTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_status_available, 0, 0)
//            holder.statusTextView.textSize = 12F
//            holder.statusTextView.text = "Available"
//        }
        setDataToFields(holder, position)
        holder.itemView.setOnClickListener {
            if (roomDetailsList[position].status == "Available") {
                listener.onItemClick(
                    roomDetailsList[position].roomId,
                    roomDetailsList[position].buildingId,
                    roomDetailsList[position].roomName,
                    roomDetailsList[position].buildingName
                )
            }
        }
    }
    private fun setDrawable(amitie: String, targetTextView: TextView) {
        when (amitie) {
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



    /**
     * it will return number of items contains in recyclerview view
     */
    override fun getItemCount(): Int {
        return roomDetailsList.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var buildingNameTextView: TextView = itemView.findViewById(R.id.building_name)
        var roomNameTextView: TextView = itemView.findViewById(R.id.room_name)
        var capacityTextView: TextView = itemView.findViewById(R.id.capacity_label)
        var statusTextView: TextView = itemView.findViewById(R.id.status_icon)
        var permissionTextView: TextView = itemView.findViewById(R.id.permission_required_text_view)
        var amenity1: TextView = itemView.findViewById(R.id.ami_room1)
        var amenity2: TextView = itemView.findViewById(R.id.ami_room2)
        var amenity3: TextView = itemView.findViewById(R.id.ami_room3)
        var amenity4: TextView = itemView.findViewById(R.id.ami_room4)
        var amenity5: TextView = itemView.findViewById(R.id.ami_room5)

        var roomDetails: RoomDetails? = null
    }

    /**
     * set data to the fields of view
     */
    @SuppressLint("SetTextI18n")
    private fun setDataToFields(holder: ViewHolder, position: Int) {
        holder.roomDetails = roomDetailsList[position]
        holder.buildingNameTextView.text = roomDetailsList[position].buildingName + ", " + roomDetailsList[position].place
        holder.roomNameTextView.text = roomDetailsList[position].roomName
        holder.capacityTextView.text = roomDetailsList[position].capacity.toString()
    }

    /**
     * an Interface which needs to be implemented whenever the adapter is attached to the recyclerview
     */
    interface ItemClickListener {
        fun onItemClick(roomId: Int?, buidingId: Int?, roomName: String?, buildingName: String?)
    }

}

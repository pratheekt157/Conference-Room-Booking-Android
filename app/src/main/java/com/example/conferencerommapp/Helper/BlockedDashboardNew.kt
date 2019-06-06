package com.example.conferencerommapp.Helper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Optional
import com.example.conferencerommapp.Blocked
import com.example.conferencerommapp.R


class BlockedDashboardNew(private val blockedList: List<Blocked>, val mContext: Context, val listener: UnblockRoomListener) : androidx.recyclerview.widget.RecyclerView.Adapter<BlockedDashboardNew.ViewHolder>() {

    private var currentPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.block_dashboard_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setDataToFields(holder, position)
        setFunctionOnButton(holder, position)
        holder.itemView.setOnClickListener {
            blockedList[position].roomId
        }
    }
    override fun getItemCount(): Int {
        return blockedList.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val conferenceName : TextView = itemView.findViewById(R.id.block_conferenceRoomName)
        val purpose : TextView = itemView.findViewById(R.id.block_purpose)
        val fromtime : TextView = itemView.findViewById(R.id.block_from_time)
        val date : TextView= itemView.findViewById(R.id.block_date)
        val unblock :TextView = itemView.findViewById(R.id.unblock)
        var blocked: Blocked? = null
    }

    @SuppressLint("SetTextI18n")
    fun setDataToFields(holder: ViewHolder, position: Int) {
        holder.blocked = blockedList[position]
        holder.conferenceName.text = blockedList[position].roomName + ", " + blockedList[position].buildingName
        holder.purpose.text = blockedList[position].purpose
        holder.date.text = blockedList[position].fromTime!!.split("T")[0]
        holder.fromtime.text = blockedList[position].fromTime!!.split("T")[1] + " - " + blockedList[position].toTime!!.split("T")[1]

    }

    private fun unBlockRoom(bookingId: Int){
        listener.onClickOfUnblock(bookingId)
    }

    private fun setFunctionOnButton(holder: ViewHolder, position: Int){
        holder.unblock.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Confirm ")
            builder.setMessage("Are you sure you want to unblock the Room?")
            builder.setPositiveButton("Yes"){_,_ ->
                unBlockRoom(blockedList[position].bookingId!!)
            }
            builder.setNegativeButton("No"){_, _ ->
            }
            val dialog: AlertDialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
            ColorOfDialogButton.setColorOfDialogButton(dialog)
        }
    }

    interface UnblockRoomListener {
        fun onClickOfUnblock(bookingId: Int)
    }


}
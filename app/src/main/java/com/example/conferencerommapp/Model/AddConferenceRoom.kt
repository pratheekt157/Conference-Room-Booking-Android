package com.example.conferencerommapp

import com.google.gson.annotations.SerializedName

//Model Class Of the AddConference
data class AddConferenceRoom(
   @SerializedName("BuildingId")
   var  bId:Int?= 0,

   @SerializedName("RoomName")
   var roomName :String?=null,

   @SerializedName("Capacity")
   var capacity :Int? = 0,

   @SerializedName("Projector")
   var projector: Boolean? = null,


   @SerializedName("Monitor")
   var monitor: Boolean? = null,

   @SerializedName("Speaker")
   var speaker: Boolean? = null,

   @SerializedName("ExtensionBoard")
   var extensionBoard: Boolean? = null,

   @SerializedName("WhiteBoardMarker")
   var whiteBoardMarker: Boolean? = null,

   @SerializedName("Permission")
   var permission: Boolean? = false

)
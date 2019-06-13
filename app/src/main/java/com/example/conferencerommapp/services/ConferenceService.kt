package com.example.conferencerommapp.services

import com.example.conferencerommapp.Blocked
import com.example.conferencerommapp.Model.*
import com.example.conferencerommapp.AddConferenceRoom
import com.example.conferencerommapp.Model.InputDetailsForRoom
import com.example.myapplication.Models.ConferenceList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ConferenceService {

    @PUT("api/ChangeStatus")
    fun chengerStatusOfEditBooking(
        @Header("Token") token: String,
        @Header("UserId") userId: String,
        @Body mEditBookingStatus: EditBookingStatus
    ): Call<ResponseBody>

    @GET("api/Building")
    fun getBuildingList(
        @Header("Token") token: String
    ): Call<List<Building>>

    @POST("api/AvailableRooms")
    fun getConferenceRoomList(
        @Header("Token") token: String,
        @Body availableRoom: InputDetailsForRoom
    ): Call<List<RoomDetails>>


    @POST("api/SuggestedRooms")
    fun getSuggestedRooms(
        @Header("Token") token: String,
        @Body availableRoom: InputDetailsForRoom
    ): Call<List<RoomDetails>>


    @POST("api/SuggestionRecurringMeeting")
    fun getSuggestedRoomsForRecurring(
        @Header("Token") token: String,
        @Body availableRoom: ManagerConference
    ): Call<List<RoomDetails>>

    @GET("api/Login")
    fun getRequestCode(
        @Header("Token") token: String,
        @Query("deviceId") deviceId: String
    ): Call<Int>

    @POST("api/Dashboard")
    fun getDashboard(
        @Header("Token") token: String,
        @Body bookingDashboardInput: BookingDashboardInput
    ): Call<DashboardDetails>

    @POST("api/UserLogin")
    fun addEmployee(
        @Header("Token") token: String,
        @Header("UserId") userId: String,
        @Body newEmoployee: Employee
    ): Call<ResponseBody>

    @POST("api/BookRoom")
    fun addBookingDetails(
        @Header("Token") token: String,
        @Body booking: Booking
    ): Call<ResponseBody>

    @PUT("api/CancelBooking")
    fun cancelBookedRoom(
        @Header("Token") token: String,
        @Query("MeetId") MeetId: Int?
    ): Call<ResponseBody>

    @PUT("api/CancelBooking")
    fun cancelRecurringBooking(
            @Header("Token") token: String,
            @Query("MeetId") MeetId: Int?,
            @Query("RecurringMeetId") RecurringMeetId:String
    ): Call<ResponseBody>

    @GET("api/getPasscode")
    fun getPasscode(
        @Header("token") token: String,
        @Query("GenerateNewPasscode") generateNewPasscode: Boolean
    ): Call<String>

    @GET("api/Employee")
    fun getEmployees(
        @Header("Token") token: String
    ): Call<List<EmployeeList>>

    @POST("api/BookRecurringMeeting")
    fun addManagerBookingDetails(
        @Header("Token") token: String,
        @Body managerBooking: ManagerBooking
    ): Call<ResponseBody>

    @POST("api/AvailableRoomsForRecurring")
    fun getMangerConferenceRoomList(
        @Header("Token") token: String,
        @Body availableRoom: ManagerConference
    ): Call<List<RoomDetails>>
//    // Pratheek's.....

    @POST("api/AddBuilding")
    fun addBuilding(
        @Header("Token") token: String,
        @Body newBuilding: AddBuilding
    ): Call<ResponseBody>

    @POST("api/AddRoom")
    fun addConference(
        @Header("Token") token: String,
        @Body newConferenceRoom: AddConferenceRoom
    ): Call<ResponseBody>

    @POST("api/BlockConfirmation")
    fun blockConfirmation(
        @Header("Token") token: String,
        @Body room: BlockRoom
    ): Call<BlockingConfirmation>

    @POST("api/BlockRoom")
    fun blockconference(
        @Header("Token") token: String,
        @Body room: BlockRoom
    ): Call<ResponseBody>

    @GET("api/GetBlockedRooms")
    fun getBlockedConference(
        @Header("Token") token: String
        ): Call<List<Blocked>>

    @PUT("api/UnblockRoom")
    fun unBlockingConferenceRoom(
        @Header("Token") token: String,
        @Body meetId: Int
    ): Call<ResponseBody>

    @GET("api/ConferenceRooms")
    fun conferencelist(
        @Header("Token") token: String,
        @Query("buildingId") id: Int
    ): Call<List<ConferenceList>>

    @PUT("api/UpdateBooking")
    fun update(
        @Header("Token") token: String,
        @Body updateBooking: UpdateBooking
    ): Call<ResponseBody>

    @GET("api/AccessToken")
    fun getAccessToken(
        @Body refreshToken: RefreshToken
    ): Call<RefreshToken>
}
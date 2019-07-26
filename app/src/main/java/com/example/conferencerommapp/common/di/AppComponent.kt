package com.example.conferencerommapp.common.di


import com.example.conferencerommapp.bookingDashboard.ui.UserBookingsDashboardActivity
import com.example.conferencerommapp.ConferenceRoomDashboard.ui.ConferenceDashBoard
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.addBuilding.ui.AddBuilding
import com.example.conferencerommapp.addBuilding.ui.AddingBuilding
import com.example.conferencerommapp.addConferenceRoom.ui.AddingConference
import com.example.conferencerommapp.blockDashboard.ui.BlockedDashboard
import com.example.conferencerommapp.blockRoom.ui.BlockConferenceRoomActivity
import com.example.conferencerommapp.booking.ui.InputDetailsForBookingFragment
import com.example.conferencerommapp.booking.ui.SelectMeetingMembersActivity
import com.example.conferencerommapp.manageBuildings.ui.BuildingDashboard
import com.example.conferencerommapp.checkConnection.NoInternetConnectionActivity
import com.example.conferencerommapp.recurringMeeting.ui.*
import com.example.conferencerommapp.splashScreen.ui.SplashScreen
import com.example.conferencerommapp.updateBooking.ui.UpdateBookingActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    /**
     * activity dagger injection object
     */
    fun inject(splashScreen: SplashScreen)

    fun inject(addBuilding: AddingBuilding)

    fun inject(addingConference: AddingConference)

    fun inject(buildingDashboard: BuildingDashboard)

    fun inject(blockConferenceRoomActivity: BlockConferenceRoomActivity)

    fun inject(blockedDashboard: BlockedDashboard)

    fun inject(conferenceDashBoard: ConferenceDashBoard)

    fun inject(managerBooking: ManagerSelectMeetingMembers)

    fun inject(managerBookDetails: RecurringBookingInputDetails)

    fun inject(noInternetConnectionActivity: NoInternetConnectionActivity)

    fun inject(selectMeetingMembersActivity: SelectMeetingMembersActivity)

    fun inject(signIn: SignIn)

    fun inject(updateBookingActivity: UpdateBookingActivity)

    fun inject(userBookingsDashboardActivity: UserBookingsDashboardActivity)

    fun inject(addBuildingNew: AddBuilding)

    /**
     * fragment dagger injection object
     */

    fun inject(cancelledBookingFragment: CancelledBookingFragment)

    fun inject(inputDetailsForBookingFragment: InputDetailsForBookingFragment)

    fun inject(previousBookingFragment: PreviousBookingFragment)

    fun inject(upcomingBookingFragment: UpcomingBookingFragment)


}
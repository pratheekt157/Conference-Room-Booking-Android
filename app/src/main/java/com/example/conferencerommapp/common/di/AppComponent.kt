package com.example.conferencerommapp.common.di

import com.example.conferencerommapp.Activity.*
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.buildings.ui.BuildingDashboard
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

    fun inject(newProjectManagerInput: NewProjectManagerInput)

    fun inject(managerBooking: ManagerSelectMeetingMembers)

    fun inject(noInternetConnectionActivity: NoInternetConnectionActivity)

    fun inject(selectMeetingMembersActivity: SelectMeetingMembersActivity)

    fun inject(signIn: SignIn)

    fun inject(updateBookingActivity: UpdateBookingActivity)

    fun inject(userBookingsDashboardActivity: UserBookingsDashboardActivity)

    /**
     * fragment dagger injection object
     */

    fun inject(cancelledBookingFragment: CancelledBookingFragment)

    fun inject(inputDetailsForBookingFragment: InputDetailsForBookingFragment)

    fun inject(previousBookingFragment: PreviousBookingFragment)

    fun inject(upcomingBookingFragment: UpcomingBookingFragment)
}
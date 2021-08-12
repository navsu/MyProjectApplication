package com.memandis.appnavigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

///**
// * Represents an app screen which the user can navigate to
// */
sealed class Destination() : Parcelable {
    @Parcelize
    object LauncherFlow : Destination()

    @Parcelize
    object HomeFlow : Destination()

    @Parcelize
    object OnBoardingFlow: Destination()

    /**
     * Extends this when origin screen must be known.
     */
    abstract class DestinationWithOrigin(val origin: String) : Destination()

    @Parcelize
    object HomeSecondLevel : DestinationWithOrigin("notifs")

    @Parcelize
    object OnBoardingSecondLevel : DestinationWithOrigin("login")

}
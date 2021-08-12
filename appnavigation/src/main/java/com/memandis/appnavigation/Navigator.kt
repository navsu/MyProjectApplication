package com.memandis.appnavigation

import androidx.navigation.NavController

class Navigator {
    lateinit var navController: NavController
//
//    // navigate on main thread or nav component crashes sometimes
//    fun navigateToFlow(navigationFlow: Destination) = when (navigationFlow) {
//        Destination.LauncherFlow -> navController.navigate(MainNavGraphDirections.
//                                          actionGlobalHomeFlow())
//        Destination.HomeFlow -> navController.navigate(MainNavGraphDirections.
//                                          actionGlobalHomeFlow())
//        is Destination.OnBoardingFlow -> navController.navigate(MainNavGraphDirections.
//                                          actionGlobalOnboardingFlow(navigationFlow.msg))
//    }
}
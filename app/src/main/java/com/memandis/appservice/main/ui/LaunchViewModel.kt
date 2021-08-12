package com.memandis.appservice.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memandis.appservice.main.domain.usecases.onboarding.OnboardingCompletedUseCase
import com.memandis.remote.utils.app.Result
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

//@HiltViewModel
class LaunchViewModel
    (
//@Inject constructor(
     onboardingCompletedUseCase: OnboardingCompletedUseCase
) : ViewModel() {

//    val launchDestination =  LaunchNavigatonAction.NavigateToMainActivityAction

//    val launchDestination = onboardingCompletedUseCase(Unit).map { result ->
//        if (result.data == false) {
//            LaunchNavigatonAction.NavigateToOnboardingAction
//        } else {
//            LaunchNavigatonAction.NavigateToMainActivityAction
//        }
//    }.stateIn(viewModelScope, Eagerly, Result.Loading)
}

sealed class LaunchNavigatonAction {
    object NavigateToOnboardingAction : LaunchNavigatonAction()
    object NavigateToMainActivityAction : LaunchNavigatonAction()
}
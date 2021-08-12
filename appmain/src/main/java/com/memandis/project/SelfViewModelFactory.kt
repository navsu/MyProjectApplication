package com.memandis.project

//import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.memandis.project.diary.vm.DiaryViewModel
import com.memandis.remote.datasource.RealtimeDatabaseRepository

//import com.svayantra.appdomain.domain.entities.booking.BookingEntity
//import com.svayantra.appdomain.domain.entities.project.ProjectEntity
//import com.svayantra.appdomain.domain.entities.service.ServiceEntity
//import com.memandis.appservice.main.domain.entities.user.UserEntity
//import com.svayantra.appdomain.domain.repository.booking.BookingRepository
//import com.svayantra.appdomain.domain.repository.project.ProjectRepository
//import com.svayantra.appdomain.domain.repository.service.ServiceRepository
//import com.svayantra.appdomain.domain.repository.user.UserRepository
//import com.svayantra.appdomain.domain.usecases.booking.GetBookingUseCase
//import com.svayantra.appdomain.domain.usecases.project.GetProjectUseCase
//import com.svayantra.appdomain.domain.usecases.service.GetServiceUseCase
//import com.svayantra.appdomain.domain.usecases.user.GetUserUseCase
//import com.memandis.mydezigner.main.ui.authenticate.vm.OnboardingViewModel
//import com.memandis.mydezigner.main.ui.home.vm.HomeViewModel
//import com.svayantra.core.mapper.Mapper
//import com.svayantra.core.models.Booking
//import com.svayantra.core.models.Project
//import com.svayantra.core.models.Service
//import com.svayantra.core.models.User

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class SelfViewModelFactory  constructor(
    private val _realtimeDatabaseRepository: RealtimeDatabaseRepository
//    private val userRepository: UserRepository,
//    private val getUserCase: GetUserUseCase,
//    private val currentUserMapper: Mapper<User, UserEntity>,
//    private val serviceRepository: ServiceRepository,
//    private val getServiceCase: GetServiceUseCase,
//    private val currentServiceMapper: Mapper<Service, ServiceEntity>,
//    private val projectRepository: ProjectRepository,
//    private val getProjectCase: GetProjectUseCase,
//    private val currentProjectMapper: Mapper<Project, ProjectEntity>,
//    private val bookingRepository: BookingRepository,
//    private val getBookingCase: GetBookingUseCase,
//    private val currentBookingMapper: Mapper<Booking, BookingEntity>
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {

//                    isAssignableFrom(OnboardingViewModel::class.java) ->
//                        OnboardingViewModel(getUserCase, currentUserMapper,
//                                       savedStateHandle = SavedStateHandle())
//
//                    isAssignableFrom(HomeViewModel::class.java) ->
//                        HomeViewModel(getServiceCase, currentServiceMapper)
//
//                    isAssignableFrom(BookingViewModel::class.java) ->
//                        BookingViewModel(getBookingCase, currentBookingMapper)
//
//                    isAssignableFrom(DiaryViewModel::class.java ) ->
//                        DiaryViewModel(_realtimeDatabaseRepository)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
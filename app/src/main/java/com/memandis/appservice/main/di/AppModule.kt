package com.memandis.appservice.main.di
//
//
//import com.memandis.appservice.ApplicationScope
//import com.memandis.appservice.DefaultDispatcher
//import com.svayantra.appdomain.domain.entities.booking.BookingEntity
//import com.svayantra.appdomain.domain.entities.project.ProjectEntity
//import com.svayantra.appdomain.domain.entities.service.ServiceEntity
//import com.memandis.appservice.main.domain.entities.user.UserEntity
//import com.svayantra.core.mapper.*
//import com.svayantra.core.models.Booking
//import com.svayantra.core.models.Project
//import com.svayantra.core.models.Service
//import com.svayantra.core.models.User
//import dagger.Binds
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.SupervisorJob
//import javax.inject.Singleton
//
//
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class  AppModule {
//
//    @Binds
//    abstract fun bindsBookingMapperImpl(
//        bookingMapper: BookingMapper
//    ): Mapper<Booking, BookingEntity>
//
//    @Binds
//    abstract fun bindsProjectMapperImpl(
//        projectMapper: ProjectMapper
//    ): Mapper<Project, ProjectEntity>
//
//    @Binds
//    abstract fun bindsServiceMapperImpl(
//        serviceMapper: ServiceMapper
//    ): Mapper<Service, ServiceEntity>
//
//    @Binds
//    abstract fun bindsUserMapperImpl(
//        userMapper: UserMapper
//    ): Mapper<User, UserEntity>
//
//
//
//}
//
//
//@InstallIn(SingletonComponent::class)
//@Module
//class AppModuleProvides {
//
//    @ApplicationScope
//    @Singleton
//    @Provides
//    fun providesApplicationScope(
//        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
//    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
//
////    @Singleton
////    @Provides
////    @MainThreadHandler
////    fun provideMainThreadHandler(): IOSchedHandler = IOSchedMainHandler()
//}
//
//
////@Module
////@InstallIn(ViewModelComponent::class)
////internal object ViewModelMovieModule {
////    @Provides
////    @ViewModelScoped
////    fun provideRepo(handle: SavedStateHandle) =
////        MovieRepository(handle.getString("movie-id"));
////}
////
////@Module
////@InstallIn(SingletonComponent::class)
////object DispatcherModule {
////
////    @DefaultDispatcher
////    @Provides
////    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
////
////    @IoDispatcher
////    @Provides
////    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
////
////    @MainDispatcher
////    @Provides
////    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
////}
////
////@Qualifier
////@Retention(AnnotationRetention.BINARY)
////annotation class IoDispatcher
////
////@Qualifier
////@Retention(AnnotationRetention.BINARY)
////annotation class MainDispatcher
////
////@Qualifier
////@Retention(AnnotationRetention.BINARY)
////annotation class DefaultDispatcher
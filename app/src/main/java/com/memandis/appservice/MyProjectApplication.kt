package com.memandis.appservice

import android.app.Application

class MyProjectApplication: Application()/*MultiDexApplication()*/ {


//    // An application that lazily provides a repository. Note that this Service Locator pattern is
//    // used to simplify the sample. Consider a Dependency Injection framework.
//    // Depends on the flavor,
//    val userRepository: UserRepository
//        get() = ServiceLocator.provideMyDezignerUserRepository(this)
//
//    val serviceRepository: ServiceRepository
//        get() = ServiceLocator.provideMyDezignerServiceRepository(this)
//
//    val projectRepository: ProjectRepository
//        get() = ServiceLocator.provideMyDezignerProjectRepository(this)
//
//    val bookingRepository: BookingRepository
//        get() = ServiceLocator.provideMyDezignerBookingRepository(this)
//
//    override fun onCreate() {
//        super.onCreate()
//
//        AndroidThreeTen.init(this)
//        RxJavaPlugins.setErrorHandler(Functions.emptyConsumer())
//    }

}
package com.memandis.appservice.main.domain.usecases.signin

//import com.google.samples.apps.iosched.shared.data.signin.AuthenticatedUserInfo
//import com.google.samples.apps.iosched.shared.data.signin.AuthenticatedUserInfoBasic
//import com.google.samples.apps.iosched.shared.data.signin.FirebaseRegisteredUserInfo
//import com.google.samples.apps.iosched.shared.data.signin.datasources.AuthStateUserDataSource
//import com.google.samples.apps.iosched.shared.data.signin.datasources.RegisteredUserDataSource
//import com.google.samples.apps.iosched.shared.di.ApplicationScope
//import com.google.samples.apps.iosched.shared.di.IoDispatcher
//import com.google.samples.apps.iosched.shared.domain.FlowUseCase
//import com.google.samples.apps.iosched.shared.fcm.TopicSubscriber
//import com.google.samples.apps.iosched.shared.result.Result
//import com.google.samples.apps.iosched.shared.result.Result.Success
//import com.google.samples.apps.iosched.shared.result.data
//import com.google.samples.apps.iosched.shared.util.cancelIfActive
//import com.svayantra.appcommon.binding.cancelIfActive
//import com.svayantra.appcommon.ApplicationScope
//import com.svayantra.appcommon.IoDispatcher
//import com.memandis.appservice.main.domain.entities.signin.AuthenticatedUserInfo
//import com.memandis.appservice.main.domain.usecases.FlowUseCase
//import javax.inject.Inject
//import javax.inject.Singleton
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.channels.ProducerScope
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.flow.shareIn
//import kotlinx.coroutines.launch
//import com.memandis.appservice.main.domain.entities.signin.AuthenticatedUserInfoBasic

/**
 * A [FlowUseCase] that observes two data sources to generate an [AuthenticatedUserInfo]
 * that includes whether the user is registered (is an attendee).
 *
 * [AuthStateUserDataSource] provides general user information, like user IDs, while
 * [RegisteredUserDataSource] observes a different data source to provide a flag indicating
 * whether the user is registered.
 */
//@Singleton
//open class ObserveUserAuthStateUseCase @Inject constructor(
////    private val registeredUserDataSource: RegisteredUserDataSource,
////    private val authStateUserDataSource: AuthStateUserDataSource,
////    private val topicSubscriber: TopicSubscriber,
//    @ApplicationScope private val externalScope: CoroutineScope,
//    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
//) : FlowUseCase<Any, AuthenticatedUserInfo>(ioDispatcher) {
//
//
//    private var observeUserRegisteredChangesJob: Job? = null
//    // As a separate coroutine needs to listen for user registration changes and emit to the
//    // flow, a callbackFlow is used
//    private val authStateChanges = callbackFlow<Result<AuthenticatedUserInfo>> {
////        authStateUserDataSource.getBasicUserInfo().collect { userResult ->
////             //Cancel observing previous user registered changes
////            observeUserRegisteredChangesJob.cancelIfActive()
//
////            if (userResult is Success) {
////                if (userResult.data != null) {
////                    processUserData(userResult.data)
////                } else {
////                    send(Success(FirebaseRegisteredUserInfo(null, false)))
////                }
////            } else {
////                send(Result.Error(Exception("FirebaseAuth error")))
////            }
////        }
//
//        // Always wait for the flow to be closed. Specially important for tests.
//        awaitClose { observeUserRegisteredChangesJob.cancelIfActive() }
//    }
//        .shareIn(externalScope, SharingStarted.WhileSubscribed())
//
//    override fun execute(parameters: Any): Flow<Result<AuthenticatedUserInfo>> = authStateChanges
//
//    private fun subscribeToRegisteredTopic() {
////        topicSubscriber.subscribeToAttendeeUpdates()
//    }
//
//    private fun unsubscribeFromRegisteredTopic() {
////        topicSubscriber.unsubscribeFromAttendeeUpdates()
//    }
//
//    private suspend fun ProducerScope<Result<AuthenticatedUserInfo>>.processUserData(
//        userData: AuthenticatedUserInfoBasic
//    ) {
//        if (!userData.isSignedIn()) {
//            userSignedOut(userData)
//        } else if (userData.getUid() != null) {
//            userSignedIn(userData.getUid()!!, userData)
//        } else {
////            send(Success(FirebaseRegisteredUserInfo(userData, false)))
//        }
//    }
//
//    private suspend fun ProducerScope<Result<AuthenticatedUserInfo>>.userSignedIn(
//        userId: String,
//        userData: AuthenticatedUserInfoBasic
//    ) {
//        // Observing the user registration changes from another scope to able to listen
//        // for this and updates to getBasicUserInfo() simultaneously
//        observeUserRegisteredChangesJob = externalScope.launch(ioDispatcher) {
//            // Start observing the user in Firestore to fetch the `registered` flag
////            registeredUserDataSource.observeUserChanges(userId).collect { result ->
////                val isRegisteredValue: Boolean? = result.data
////                // When there's new user data and the user is an attendee, subscribe to topic:
////                if (isRegisteredValue == true && userData.isSignedIn()) {
////                    subscribeToRegisteredTopic()
////                }
////
//////                send(Success(FirebaseRegisteredUserInfo(userData, isRegisteredValue)))
////            }
//        }
//    }
//
//    private suspend fun ProducerScope<Result<AuthenticatedUserInfo>>.userSignedOut(
//        userData: AuthenticatedUserInfoBasic?
//    ) {
////        send(Success(FirebaseRegisteredUserInfo(userData, false)))
//        unsubscribeFromRegisteredTopic() // Stop receiving notifications for attendees
//    }
//}
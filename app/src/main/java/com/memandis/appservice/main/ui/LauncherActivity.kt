package com.memandis.appservice.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.memandis.appbooking.BookingActivity
import com.memandis.appnavigation.NavigationViewModel
import com.memandis.appnavigation.Navigator
import com.memandis.onboarding.OnboardingActivity
import com.memandis.project.main.MainActivity
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


/**
 * A 'Trampoline' activity for sending users to an appropriate screen on launch.
 */
//@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val viewModel: LaunchViewModel by viewModels()
//        val viewModel = ViewModelProvider(this).get( LaunchViewModel::class.java)

        // [START firebase_options]
        // Manually configure Firebase Options. The following fields are REQUIRED:
        //   - Project ID
        //   - App ID
        //   - API Key
        //   - ...
        //    .build()
        // [END firebase_options]
        // You can find all the below details in the google-services.json file,
        // NOTE: All details are fake and random created API keys,
        // please use your in the downloaded file.
        val options = FirebaseOptions.Builder()
            .setProjectId("appservice-eb5e1")
            .setApplicationId("1:831072303749:android:0fb55d0133645dfd2287ec")
            .setApiKey("AIzaSyBvxwq-t8dLGJO13GHyAWf9ku4IrAnoycE")
            .setDatabaseUrl("https://appservice-eb5e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .setStorageBucket("gs://appservice-eb5e1.appspot.com/")
            .build()

        Firebase.initialize(this, options, "MyProjectApplication")

//        // [START firebase_secondary]
//        // Initialize tertiary FirebaseApp.
//        Firebase.initialize(this , options, "onboarding")
//
//        // [START firebase_tertiary]
//        // Initialize tertiary FirebaseApp.
//        Firebase.initialize(this , options, "project")
        
        lifecycleScope.launch {
//
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.launchDestination.collect {action ->
//                    when (action) {
//                        is LaunchNavigatonAction.NavigateToMainActivityAction ->
//                        startActivity(Intent(this@LauncherActivity, MainActivity::class.java) )
//                        is LaunchNavigatonAction.NavigateToOnboardingAction ->
//                        startActivity(Intent(this@LauncherActivity, OnboardingActivity::class.java) )
                 startActivity(Intent(this@LauncherActivity, BookingActivity::class.java) )
//                    }
//                    finish()
//                }
//
//            }

        }

    }


}
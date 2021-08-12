package com.memandis.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.memandis.onboarding.databinding.ActivityOnboardingBinding
import com.memandis.onboarding.viewmodels.RegisterViewModel

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [START firebase_options]
        // Manually configure Firebase Options. The following fields are REQUIRED:
        //   - Project ID
        //   - App ID
        //   - API Key
        //   - ...
        //    .build()
        // [END firebase_options]
        //Y ou can find all the below details in the google-services.json file,
        // NOTE: All details are fake and random created API keys,
        // please use your in the downloaded file.
//        val options = FirebaseOptions.Builder()
//            .setProjectId("appservice-eb5e1")
//            .setApplicationId("1:831072303749:android:0fb55d0133645dfd2287ec")
//            .setApiKey("AIzaSyBvxwq-t8dLGJO13GHyAWf9ku4IrAnoycE")
//            .setDatabaseUrl("https://appservice-eb5e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
//            .setStorageBucket("gs://appservice-eb5e1.appspot.com/")
//            .build()
//
//        // [START firebase_secondary]
//        // Initialize secondary FirebaseApp.
//        Firebase.initialize(this, options, "onboarding")

//        // Retrieve secondary FirebaseApp.
//        val secondary = Firebase.app("onboarding")
//        // Get the database for the other app.
//        val secondaryDatabase = Firebase.database(secondary)
//        // [END firebase_secondary]

        ViewModelProvider(this).get( RegisterViewModel::class.java)

    }

}

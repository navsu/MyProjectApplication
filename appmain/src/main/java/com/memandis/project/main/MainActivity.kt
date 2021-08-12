package com.memandis.project.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.memandis.appmain.R
import com.memandis.appmain.databinding.ActivityMainBinding
import com.memandis.appnavigation.Destination
import com.memandis.appnavigation.NavigationViewModel
import com.memandis.appnavigation.Navigator
import com.memandis.appnavigation.setFragmentNavigationListener
import com.memandis.project.diary.vm.DiaryDateViewModel
import com.memandis.project.diary.vm.DiaryViewModel
import com.memandis.project.viewModelInjectionHelper
import com.memandis.remote.datasource.FirebaseUserRepository
import com.memandis.remote.datasource.FirebaseUserRepository.attachUserToFirebaseRepositories
import kotlinx.coroutines.ExperimentalCoroutinesApi


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private val navigator: Navigator = Navigator()

//    private lateinit var navViewModel : NavigationViewModel

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        FirebaseApp.initializeApp(this);
//        val userid = "AVbZHhrwcVRMFAlcQ3U3nqELqE93"
        binding = ActivityMainBinding.inflate(layoutInflater)
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
//        // Initialize tertiary FirebaseApp.
//        Firebase.initialize(this, options, "MyProjectApplication")
//
//        // Retrieve tertiary FirebaseApp.
//        val tertiary = Firebase.app("project")
//        // Get the database for the other app.
//        val tertiaryDatabase = Firebase.database(tertiary)
//        // [END firebase_tertiary]

        // Attach user credential to the application repo
        attachUserToFirebaseRepositories()
//        FirebaseUserRepository.attachDefaultUserToFirebaseRepositories("qTLOZ115xpMNXpJhuQdCLrjOe5y2")

        viewModelInjectionHelper<DiaryViewModel>(this)
        viewModelInjectionHelper<DiaryDateViewModel>(this)

        binding.bottomNavigation.setupWithNavController(navController)

//        navigator.navController = navController
//        navViewModel = ViewModelProvider(this).get(NavigationViewModel::class.java)

        setupListeners()

    }

//    @ExperimentalCoroutinesApi
    private fun setupListeners() {

       binding.bottomNavigation.setOnItemSelectedListener {menuItem ->
        // if(menuItem.itemId != R.id.diaryFragment
        //    || menuItem.itemId != R.id.searchFragment){
        //     return@setOnItemSelectedListener false
        // }
//            var selectedFragment: Fragment? = null
//            when (item.itemId) {
////                R.id.bottom_designer_gallery -> selectedFragment = HomeFragment()
////                R.id.bottom_designer_meeting -> selectedFragment = AppointmentFragment()
////                R.id.bottom_designer_info -> selectedFragment = InfoFragment()
//            }
////            drawerLayout.closeDrawer(GravityCompat.START)
//            supportFragmentManager.beginTransaction().replace(
//                R.id.nav_host_fragment,
//                selectedFragment!!
//            ).commit()
//            true
        onNavDestinationSelected(menuItem, navController)
        return@setOnItemSelectedListener true
    }
//        supportFragmentManager.setFragmentNavigationListener(this) { destination ->
//            val navController = findNavController(R.id.mainNavHostFragment)
//            when (destination) {
//
//                is Destination.HomeFlow -> {
//                    val intent = Intent(this, MainActivity::class.java)
//                    this.startActivity(intent)
//                    navController.navigate(R.id.action_global_navigation_home)
//                }
//                is Destination.OnBoardingFlow -> {
//                    navController.navigate(R.id.action_global_navigation_dashboard)
//                }
//
//                is Destination.Notifications -> {
//                    val  args = NotificationsFragment.generateArgs(destination.someData)
//                    navController.navigate(R.id.action_navigation_dashboard_to_navigation_notifications, args)
//                }
//
//                is Destination.DestinationWithOrigin -> {
//                    navController.navigate(R.id.action_navigation_notifications_to_secondLevelFragment2)
//                }
//
//                is Destination.DestinationWithOrigin -> {
//                    navController.navigate(R.id.action_loginFragment_to_loginEmailFragment)
//                }
//
//                else ->
//            }
//        }

//        supportFragmentManager.setFragmentResultListener(
//            LoginFragment.REQ_KEY, this) { key, bundle ->
//            val result = bundle.toDashBoardResult()
//            Toast.makeText(this, "Received: ${result.data}", Toast.LENGTH_LONG).show()
//        }
//
//        binding.bottomNavigation.setOnNavigationItemReselectedListener {
//            navViewModel.onSomethingInterestingHappen()
//        }

    }


}

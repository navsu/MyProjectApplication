package com.memandis.appbooking.scheduling

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.memandis.appbooking.R
import com.memandis.appbooking.databinding.ActivityBookingBinding
import com.memandis.appbooking.databinding.ActivityScheduleBinding

class SchedulingActivity : AppCompatActivity() {

    companion object {
        val EXTRA_PROFESSIONAL = "Professional"
    }
    private lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}

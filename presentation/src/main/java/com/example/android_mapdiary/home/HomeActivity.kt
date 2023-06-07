package com.example.android_mapdiary.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.android_mapdiary.R
import com.example.android_mapdiary.common.ViewBindingActivity
import com.example.android_mapdiary.databinding.ActivityHomeBinding

class HomeActivity : ViewBindingActivity<ActivityHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nhf =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = nhf.navController

        binding.bottomNav.setupWithNavController(navController)
    }
}
package com.nacho.restaurantapplication.presentation.activity.splash

import android.content.Intent
import android.os.Bundle
import kotlinx.coroutines.*
import androidx.appcompat.app.AppCompatActivity
import com.nacho.restaurantapplication.databinding.ActivitySplashBinding
import com.nacho.restaurantapplication.presentation.activity.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope.launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

}
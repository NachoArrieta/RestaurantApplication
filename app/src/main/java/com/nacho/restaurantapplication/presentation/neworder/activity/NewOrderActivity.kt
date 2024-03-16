package com.nacho.restaurantapplication.presentation.neworder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nacho.restaurantapplication.databinding.ActivityNewOrderBinding
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import com.nacho.restaurantapplication.presentation.home.activity.HomeActivity

class NewOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            newOrderToolbar.toolbarImgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

        }

        //OnBackPressed
        val intent = Intent(this, HomeActivity::class.java)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        })

    }

}
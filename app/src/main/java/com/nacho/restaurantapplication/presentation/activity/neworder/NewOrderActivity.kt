package com.nacho.restaurantapplication.presentation.activity.neworder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nacho.restaurantapplication.databinding.ActivityNewOrderBinding
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.presentation.activity.home.HomeActivity
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewOrderBinding
    private val newOrderVM: NewOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProducts()

        val navController = supportFragmentManager.findFragmentById(R.id.new_order_container)
            ?.findNavController()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed(navController)
            }
        })

        binding.toolbarImgBack.setOnClickListener {
            handleBackPressed(navController)
        }

    }

    private fun getProducts() {
        with(newOrderVM) {
            fetchBurgers()
            fetchPromotions()
            fetchDrinks()
            fetchDesserts()
            fetchAccompaniments()
            fetchToppingsAndDressings()
        }
    }

    private fun handleBackPressed(navController: NavController?) {
        if (navController != null && navController.navigateUp()) {
            return
        } else {
            val intent = Intent(this@NewOrderActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

}
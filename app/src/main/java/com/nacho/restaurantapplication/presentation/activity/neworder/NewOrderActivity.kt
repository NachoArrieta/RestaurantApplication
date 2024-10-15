package com.nacho.restaurantapplication.presentation.activity.neworder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nacho.restaurantapplication.databinding.ActivityNewOrderBinding
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.presentation.activity.home.HomeActivity
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
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
                if (navController != null && navController.navigateUp()) {
                    // Si puede navegar hacia atrás dentro del NavController, lo hace
                    return
                } else {
                    // Si no hay más fragmentos en la pila, navega al HomeActivity
                    val intent = Intent(this@NewOrderActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }
            }
        })

    }

    private fun getProducts() {
        with(newOrderVM) {
            fetchBurgers()
            fetchPromotions()
            fetchDrinks()
            fetchDesserts()
            fetchAccompaniments()
        }
    }

}
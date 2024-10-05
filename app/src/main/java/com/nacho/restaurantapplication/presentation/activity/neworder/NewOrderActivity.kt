package com.nacho.restaurantapplication.presentation.activity.neworder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nacho.restaurantapplication.databinding.ActivityNewOrderBinding
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nacho.restaurantapplication.presentation.activity.home.HomeActivity
import com.nacho.restaurantapplication.presentation.adapter.neworder.ViewPagerAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel

class NewOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewOrderBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val newOrderVM: NewOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerAdapter = ViewPagerAdapter(this, newOrderVM)

        setupObservers()
        setTabLayout()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@NewOrderActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        })
    }

    private fun setupObservers() {
        newOrderVM.selectedTabIndex.observe(this) { index ->
            binding.newOrderVp.setCurrentItem(index, true)
        }
    }

    private fun setTabLayout() {
        with(binding) {
            newOrderVp.adapter = viewPagerAdapter

            TabLayoutMediator(newOrderTl, newOrderVp) { tab, position ->
                tab.text = newOrderVM.sectionNames[position]
            }.attach()
            newOrderTl.tabMode = TabLayout.MODE_SCROLLABLE
            newOrderTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        newOrderVM.setSelectedTabIndex(it.position)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

}
package com.nacho.restaurantapplication.presentation.neworder.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.Fragment
import com.nacho.restaurantapplication.presentation.neworder.fragment.AccompanimentsFragment
import com.nacho.restaurantapplication.presentation.neworder.fragment.BurgersFragment
import com.nacho.restaurantapplication.presentation.neworder.fragment.DessertsFragment
import com.nacho.restaurantapplication.presentation.neworder.fragment.DrinksFragment
import com.nacho.restaurantapplication.presentation.neworder.fragment.PromotionsFragment
import com.nacho.restaurantapplication.presentation.neworder.viewmodel.NewOrderViewModel

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val newOrderViewModel: NewOrderViewModel) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return newOrderViewModel.sectionNames.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BurgersFragment()
            1 -> PromotionsFragment()
            2 -> DrinksFragment()
            3 -> AccompanimentsFragment()
            4 -> DessertsFragment()
            else -> BurgersFragment()
        }
    }
}
package com.nacho.restaurantapplication.presentation.adapter.neworder

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.Fragment
import com.nacho.restaurantapplication.presentation.fragment.neworder.AccompanimentsFragment
import com.nacho.restaurantapplication.presentation.fragment.neworder.BurgersFragment
import com.nacho.restaurantapplication.presentation.fragment.neworder.DessertsFragment
import com.nacho.restaurantapplication.presentation.fragment.neworder.DrinksFragment
import com.nacho.restaurantapplication.presentation.fragment.neworder.PromotionsFragment
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel

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
package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nacho.restaurantapplication.databinding.FragmentNewOrderBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.ViewPagerAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewOrderFragment : Fragment() {

    private var _binding: FragmentNewOrderBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val newOrderVM: NewOrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ViewPagerAdapter(this, newOrderVM)

        setupObservers()
        setTabLayout()

    }

    private fun setupObservers() {
        newOrderVM.selectedTabIndex.observe(viewLifecycleOwner) { index ->
            binding.newOrderVp.setCurrentItem(index, true)
        }
    }

    private fun setTabLayout() {
        with(binding) {
            newOrderVp.adapter = viewPagerAdapter
            newOrderVp.disableUserInput()

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

    private fun ViewPager2.disableUserInput() {
        this.isUserInputEnabled = false
    }

}
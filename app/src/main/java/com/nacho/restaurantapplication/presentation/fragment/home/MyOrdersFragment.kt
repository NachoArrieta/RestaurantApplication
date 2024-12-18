package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nacho.restaurantapplication.databinding.FragmentMyOrdersBinding
import com.nacho.restaurantapplication.presentation.adapter.home.OrderAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrdersFragment : Fragment() {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

    private val homeVM: HomeViewModel by activityViewModels()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = homeVM.userId.value
        if (user != null) {
            homeVM.fetchUserOrders(user)
        }

        setupObservers()

    }

    private fun setupObservers() {

        homeVM.userOrders.observe(viewLifecycleOwner) { orders ->
            binding.myOrdersRv.layoutManager = LinearLayoutManager(context)
            orderAdapter = OrderAdapter(orders)
            binding.myOrdersRv.adapter = orderAdapter
        }

    }

}
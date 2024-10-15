package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.databinding.FragmentDessertsBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.DessertAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DessertsFragment : Fragment() {

    private var _binding: FragmentDessertsBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()

    private lateinit var bsbList: DessertAdapter
    private lateinit var sundaesList: DessertAdapter
    private lateinit var milkshakesList: DessertAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDessertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

    }

    private fun setupObservers() {

        newOrderVM.desserts.observe(viewLifecycleOwner) { desserts ->
            desserts?.let { categories ->
                bsbList = DessertAdapter(categories["Bsb"] ?: emptyList()) { /* Manejar click */ }
                sundaesList = DessertAdapter(categories["Sundaes"] ?: emptyList()) { /* Manejar click */ }
                milkshakesList = DessertAdapter(categories["Milkshakes"] ?: emptyList()) { /* Manejar click */ }

                with(binding) {
                    dessertsRvDesserts.adapter = bsbList
                    dessertsRvSundaes.adapter = sundaesList
                    dessertsRvMilkshakes.adapter = milkshakesList
                }
            }
        }

        newOrderVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setupViews(isLoading)
        }

    }

    private fun setupViews(isLoading: Boolean) {
        with(binding) {
            val visibility = if (isLoading) View.GONE else View.VISIBLE

            val textViews = listOf(dessertsTxtDesserts, dessertsTxtSundaes, dessertsTxtMilkshakes)
            val recyclerViews = listOf(dessertsRvDesserts, dessertsRvSundaes, dessertsRvMilkshakes)

            textViews.forEach { it.visibility = visibility }
            recyclerViews.forEach { it.visibility = visibility }

            dessertsShimmer.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}
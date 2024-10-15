package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.databinding.FragmentDrinksBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.DrinkAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrinksFragment : Fragment() {

    private var _binding: FragmentDrinksBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()

    private lateinit var beerList: DrinkAdapter
    private lateinit var sodaList: DrinkAdapter
    private lateinit var flavoredList: DrinkAdapter
    private lateinit var waterList: DrinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrinksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

    }

    private fun setupObservers() {

        newOrderVM.drinks.observe(viewLifecycleOwner) { drinks ->
            drinks?.let { categories ->
                beerList = DrinkAdapter(categories["Beer"] ?: emptyList()) { /* Manejar click */ }
                sodaList = DrinkAdapter(categories["Soda"] ?: emptyList()) { /* Manejar click */ }
                flavoredList = DrinkAdapter(categories["Flavored"] ?: emptyList()) { /* Manejar click */ }
                waterList = DrinkAdapter(categories["Water"] ?: emptyList()) { /* Manejar click */ }

                with(binding) {
                    drinksRvBeers.adapter = beerList
                    drinksRvSodas.adapter = sodaList
                    drinksRvFlavored.adapter = flavoredList
                    drinksRvWater.adapter = waterList
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

            val textViews = listOf(drinksTxtBeers, drinksTxtSodas, drinksTxtFlavored, drinksTxtWater)
            val recyclerViews = listOf(drinksRvBeers, drinksRvSodas, drinksRvFlavored, drinksRvWater)

            textViews.forEach { it.visibility = visibility }
            recyclerViews.forEach { it.visibility = visibility }

            drinksShimmer.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}
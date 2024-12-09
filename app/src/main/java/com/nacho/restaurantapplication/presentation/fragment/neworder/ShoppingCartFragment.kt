package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.databinding.FragmentShoppingCartBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.ShoppingCartAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private lateinit var shoppingCartList: ShoppingCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        newOrderVM.cartItems.observe(viewLifecycleOwner) { cartItems ->
            if (cartItems.isNotEmpty()) {
                shoppingCartList = ShoppingCartAdapter(cartItems) { item ->
                    newOrderVM.removeFromCart(item)
                }
                binding.shoppingCartRv.adapter = shoppingCartList
            } else {
                findNavController().navigateUp()
            }
        }
    }

}
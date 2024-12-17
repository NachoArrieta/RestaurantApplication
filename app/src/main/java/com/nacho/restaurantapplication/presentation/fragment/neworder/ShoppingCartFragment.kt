package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.extensions.onTextChanged
import com.nacho.restaurantapplication.databinding.FragmentShoppingCartBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.ShoppingCartAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private val paymentMethodVM: PaymentMethodViewModel by activityViewModels()

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

        newOrderVM.setToolbarTitle(getString(R.string.toolbar_title_shopping_cart))
        newOrderVM.setToolbarVisibility(false)
        setupObservers()
        setupListeners()

    }

    private fun setupObservers() {

        newOrderVM.cartItems.observe(viewLifecycleOwner) { cartItems ->
            if (cartItems.isNotEmpty()) {
                shoppingCartList = ShoppingCartAdapter(
                    cartItems,
                    onDeleteItem = { item -> newOrderVM.removeFromCart(item) },
                    onQuantityChanged = { item, newQuantity -> newOrderVM.updateItemQuantity(item, newQuantity) }
                )
                binding.shoppingCartRv.adapter = shoppingCartList
            } else {
                findNavController().navigateUp()
            }
        }

        newOrderVM.cartTotalPrice.observe(viewLifecycleOwner) { total ->
            binding.apply {
                shoppingCartTxtTotal.text = getString(R.string.neworder_shopping_cart_total, total)
                shoppingCartCvInformationPayment.visibility = if (total == 0) View.GONE else View.VISIBLE
            }
            newOrderVM.calculateTotalPrice()
        }

        newOrderVM.discountAmount.observe(viewLifecycleOwner) { discount ->
            binding.shoppingCartTxtDiscount.text = getString(R.string.neworder_discount, discount)
            newOrderVM.calculateTotalPrice()
        }

        newOrderVM.finalTotalPrice.observe(viewLifecycleOwner) { finalTotal ->
            binding.shoppingCartTxtPriceTotal.text = getString(R.string.neworder_total_price, finalTotal)
        }

    }

    private fun setupListeners() {
        with(binding) {

            shoppingCartTieDiscount.onTextChanged { inputCouponCode ->
                val trimmedCoupon = inputCouponCode.trim()
                if (trimmedCoupon.isEmpty()) {
                    shoppingCartTilDiscount.error = null
                    newOrderVM.validateCoupon("")
                } else {
                    val isValid = newOrderVM.validateCoupon(trimmedCoupon)
                    shoppingCartTilDiscount.error = if (isValid) null else getString(R.string.neworder_error_invalid_coupon)
                }
            }

            shoppingCartBtn.setOnClickListener {
                paymentMethodVM.userCards.observe(viewLifecycleOwner) { userCards ->
                    if (!userCards.isNullOrEmpty()) findNavController().navigate(R.id.action_shoppingCartFragment_to_selectedPaymentMethodFragment)
                    else findNavController().navigate(R.id.action_shoppingCartFragment_to_addCardFragment)
                }
            }

        }
    }

}
package com.nacho.restaurantapplication.presentation.fragment.neworder.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentConfirmPaymentBinding
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPaymentFragment : Fragment() {

    private var _binding: FragmentConfirmPaymentBinding? = null
    private val binding get() = _binding!!

    private val homeVM: HomeViewModel by activityViewModels()
    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private val paymentMethodVM: PaymentMethodViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newOrderVM.setToolbarTitle(getString(R.string.toolbar_title_confirm))
        setupObservers()

    }

    private fun setupObservers() {

        newOrderVM.cartTotalPrice.observe(viewLifecycleOwner) { productsPrice ->
            binding.confirmPaymentTxtTotal.text = getString(R.string.confirm_payment_product_price, productsPrice)
        }

        newOrderVM.discountAmount.observe(viewLifecycleOwner) { discount ->
            binding.confirmPaymentTxtDiscount.text = getString(R.string.confirm_payment_discount_price, discount)
        }

        paymentMethodVM.deliveryMethod.observe(viewLifecycleOwner) { deliveryMethod ->
            if (deliveryMethod.shippingPrice != 0) binding.confirmPaymentTxtShipment.text =
                getString(R.string.confirm_payment_product_price, deliveryMethod.shippingPrice)
            else getString(R.string.confirm_no_shipment)
        }

        calculateTotal()

    }

    private fun calculateTotal() {
        val productsPrice = newOrderVM.cartTotalPrice.value ?: 0
        val discount = newOrderVM.discountAmount.value ?: 0
        val shipmentPrice = paymentMethodVM.deliveryMethod.value?.shippingPrice ?: 0

        val total = productsPrice - discount + shipmentPrice

        val formattedTotal = "TOTAL: $${total}"
        binding.confirmPaymentTxtSubtitleTotal.text = formattedTotal
    }

}
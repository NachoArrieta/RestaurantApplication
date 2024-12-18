package com.nacho.restaurantapplication.presentation.fragment.neworder.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.Order
import com.nacho.restaurantapplication.databinding.FragmentConfirmPaymentBinding
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        binding.confirmPaymentBtn.setOnClickListener {
            confirmOrder()
            findNavController().navigate(R.id.action_confirmPaymentFragment2_to_loadingPaymentFragment)
        }
    }

    private fun setupObservers() {
        newOrderVM.cartTotalPrice.observe(viewLifecycleOwner) { productsPrice ->
            binding.confirmPaymentTxtTotal.text =
                getString(R.string.confirm_payment_product_price, productsPrice)
        }

        newOrderVM.discountAmount.observe(viewLifecycleOwner) { discount ->
            binding.confirmPaymentTxtDiscount.text =
                getString(R.string.confirm_payment_discount_price, discount)
        }

        paymentMethodVM.deliveryMethod.observe(viewLifecycleOwner) { deliveryMethod ->
            if (deliveryMethod.shippingPrice != 0) binding.confirmPaymentTxtShipment.text =
                getString(R.string.confirm_payment_product_price, deliveryMethod.shippingPrice)
            else binding.confirmPaymentTxtShipment.text = getString(R.string.confirm_no_shipment)
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

    private fun confirmOrder() {
        val uid = paymentMethodVM.uId.value ?: return
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val currentHour = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val productList = newOrderVM.cartItems.value ?: listOf()
        val paymentInfo = paymentMethodVM.selectedCard.value ?: return
        val shipmentInfo = paymentMethodVM.deliveryMethod.value ?: return
        val discountInfo = newOrderVM.discountAmount.value ?: 0

        val productsPrice = newOrderVM.cartTotalPrice.value ?: 0
        val shipmentPrice = shipmentInfo.shippingPrice
        val totalInfo = productsPrice - discountInfo + shipmentPrice

        val newOrder = Order(
            date = currentDate,
            hour = currentHour,
            productList = productList,
            paymentInfo = paymentInfo,
            shipmentInfo = shipmentInfo,
            discountInfo = discountInfo,
            totalInfo = totalInfo
        )

        paymentMethodVM.addOrder(uid, newOrder)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
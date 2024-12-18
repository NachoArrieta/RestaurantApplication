package com.nacho.restaurantapplication.presentation.fragment.neworder.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentSelectedPaymentMethodBinding
import com.nacho.restaurantapplication.presentation.adapter.payment.SelectedPaymentAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SelectedPaymentMethodFragment : Fragment() {

    private var _binding: FragmentSelectedPaymentMethodBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private val paymentMethodVM: PaymentMethodViewModel by activityViewModels()

    private lateinit var paymentList: SelectedPaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = paymentMethodVM.uId.value
        if (uid != null) paymentMethodVM.fetchUserCards(uid)

        newOrderVM.setToolbarTitle(getString(R.string.toolbar_title_selected_payment))

        setupObservers()
        setupListeners()

    }

    private fun setupObservers() {
        paymentMethodVM.userCards.observe(viewLifecycleOwner) { cards ->
            binding.selectedPaymentMethodLoading.visibility = View.VISIBLE
            paymentList = SelectedPaymentAdapter(cards)
            binding.selectedPaymentMethodVp.adapter = paymentList
            lifecycleScope.launch {
                delay(2000)
                binding.selectedPaymentMethodLoading.visibility = View.GONE
                binding.selectedPaymentMethodVp.visibility = View.VISIBLE
                binding.selectedPaymentMethodAdd.visibility = View.VISIBLE
                binding.selectedPaymentMethodBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListeners() {
        binding.apply {

            selectedPaymentMethodAdd.setOnClickListener {
                findNavController().navigate(R.id.action_selectedPaymentMethodFragment_to_addCardFragment)
            }

            selectedPaymentMethodBtn.setOnClickListener {
                findNavController().navigate(R.id.action_selectedPaymentMethodFragment_to_deliveryMethodFragment2)
            }

        }
    }

}
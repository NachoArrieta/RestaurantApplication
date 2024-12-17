package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentMyPaymentMethodsBinding
import com.nacho.restaurantapplication.presentation.adapter.payment.CardAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPaymentMethodsFragment : Fragment() {

    private var _binding: FragmentMyPaymentMethodsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PaymentMethodViewModel by activityViewModels()

    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPaymentMethodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = viewModel.uId.value
        if (uid != null) viewModel.fetchUserCards(uid)

        setupObservers()
        setupListeners()

    }

    private fun setupObservers() {
        viewModel.userCards.observe(viewLifecycleOwner) { cards ->
            binding.myPaymentMethodsLoading.visibility = View.VISIBLE
            lifecycleScope.launch {
                delay(3000)
                binding.myPaymentMethodsLoading.visibility = View.GONE
                if (cards.isNullOrEmpty()) {
                    binding.myPaymentMethodsRv.visibility = View.GONE
                    binding.myPaymentMethodsCvEmpty.visibility = View.VISIBLE
                    binding.myPaymentMethodsAdd.visibility = View.VISIBLE
                } else {
                    cardAdapter = CardAdapter(cards)
                    binding.myPaymentMethodsRv.adapter = cardAdapter
                    binding.myPaymentMethodsRv.visibility = View.VISIBLE
                    binding.myPaymentMethodsAdd.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupListeners() {
        binding.myPaymentMethodsAdd.setOnClickListener {
            findNavController().navigate(R.id.action_nav_my_payment_methods_to_addPaymentMethodFragment)
        }
    }

}
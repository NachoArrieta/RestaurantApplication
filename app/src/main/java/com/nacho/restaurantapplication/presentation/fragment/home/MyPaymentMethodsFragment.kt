package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentMyPaymentMethodsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPaymentMethodsFragment : Fragment() {

    private var _binding: FragmentMyPaymentMethodsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPaymentMethodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            myPaymentMethodsAdd.setOnClickListener {
                findNavController().navigate(R.id.action_nav_my_payment_methods_to_addPaymentMethodFragment)
            }

        }

    }

}
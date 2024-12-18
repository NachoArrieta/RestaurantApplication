package com.nacho.restaurantapplication.presentation.fragment.neworder.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentDeliveryMethodBinding
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel

class DeliveryMethodFragment : Fragment() {

    private var _binding: FragmentDeliveryMethodBinding? = null
    private val binding get() = _binding!!

    private val homeVM: HomeViewModel by activityViewModels()
    private val paymentMethodVM: PaymentMethodViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()

    }

    private fun setupObservers() {
        homeVM.userInformation.observe(viewLifecycleOwner) { userInfo ->
            binding.apply {

                if (userInfo != null) {

                    if (!userInfo.address.isNullOrEmpty() && !userInfo.number.isNullOrEmpty() && !userInfo.city.isNullOrEmpty()) {

                        deliveryMethodCvHomeDelivery.isClickable = true
                        val formattedAddress = "${userInfo.address} - Piso ${userInfo.floor} - Depto ${userInfo.number} - ${userInfo.city}"
                        deliveryMethodTxtAddress.text = formattedAddress

                    } else {
                        deliveryMethodCvHomeDelivery.isClickable = false
                        deliveryMethodTxtAddress.text = getString(R.string.delivery_txt_modify_address)
                    }

                } else {
                    deliveryMethodCvHomeDelivery.isClickable = false
                    deliveryMethodTxtAddress.text = getString(R.string.delivery_txt_modify_address)
                }

            }
        }
    }

    private fun setupListeners() {

        binding.apply {

            deliveryMethodTxtModify.setOnClickListener {
                if (!modifyData.isVisible) {
                    deliveryMethodCvPickItLocalOne.visibility = View.GONE
                    deliveryMethodCvPickItLocalTwo.visibility = View.GONE
                    deliveryMethodCvPickItLocalThree.visibility = View.GONE
                    modifyData.visibility = View.VISIBLE
                } else {
                    deliveryMethodCvPickItLocalOne.visibility = View.VISIBLE
                    deliveryMethodCvPickItLocalTwo.visibility = View.VISIBLE
                    deliveryMethodCvPickItLocalThree.visibility = View.VISIBLE
                    modifyData.visibility = View.GONE
                }
            }

        }

    }

}
package com.nacho.restaurantapplication.presentation.fragment.neworder.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.extensions.dismissKeyboard
import com.nacho.restaurantapplication.core.extensions.onTextChanged
import com.nacho.restaurantapplication.data.model.DeliveryMethod
import com.nacho.restaurantapplication.databinding.FragmentDeliveryMethodBinding
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryMethodFragment : Fragment() {

    private var _binding: FragmentDeliveryMethodBinding? = null
    private val binding get() = _binding!!

    private val homeVM: HomeViewModel by activityViewModels()
    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private val paymentMethodVM: PaymentMethodViewModel by activityViewModels()

    private val cities by lazy {
        listOf(
            getString(R.string.profile_rio_tercero),
            getString(R.string.profile_almafuerte),
            getString(R.string.profile_villa_del_dique)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newOrderVM.setToolbarTitle(getString(R.string.toolbar_title_shipment))
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

        with(binding) {

            modifyAtvCity.onTextChanged { validateFields() }
            profileTieAddress.onTextChanged { validateFields() }
            profileTieFloor.onTextChanged { validateFields() }
            profileTieFloorNumber.onTextChanged { validateFields() }

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

                    modifyAtvCity.text = null
                    profileTieAddress.text = null
                    profileTieFloor.text = null
                    profileTieFloorNumber.text = null

                }
            }

            modifyDataBtn.setOnClickListener {
                val newCity = modifyAtvCity.text.toString().trim()
                val newAddress = profileTieAddress.text.toString().trim()
                val newFloor = profileTieFloor.text.toString().trim()
                val newNumber = profileTieFloorNumber.text.toString().trim()

                if (newCity.isNotEmpty() && newAddress.isNotEmpty() && newFloor.isNotEmpty() && newNumber.isNotEmpty()) {
                    val formattedAddress = "$newAddress - Piso $newFloor - Depto $newNumber - $newCity"
                    deliveryMethodTxtAddress.text = formattedAddress

                    modifyData.visibility = View.GONE
                    deliveryMethodCvPickItLocalOne.visibility = View.VISIBLE
                    deliveryMethodCvPickItLocalTwo.visibility = View.VISIBLE
                    deliveryMethodCvPickItLocalThree.visibility = View.VISIBLE

                    modifyAtvCity.text = null
                    profileTieAddress.text = null
                    profileTieFloor.text = null
                    profileTieFloorNumber.text = null
                }

            }

            deliveryMethodCvHomeDelivery.setOnClickListener {
                val addressText = deliveryMethodTxtAddress.text.toString()
                if (addressText.isNotEmpty() && addressText.contains("-")) {
                    val parts = addressText.split("-").map { it.trim() }
                    if (parts.size >= 4) {
                        val address = parts[0]
                        val floor = parts[1].replace("Piso ", "").trim()
                        val number = parts[2].replace("Depto ", "").trim()
                        val city = parts[3]

                        paymentMethodVM.setDeliveryMethod(
                            DeliveryMethod(
                                type = "Envío",
                                shippingPrice = 2500,
                                address = address,
                                floor = floor,
                                number = number,
                                city = city
                            )
                        )

                    }
                }
                findNavController().navigate(R.id.action_deliveryMethodFragment2_to_confirmPaymentFragment2)
            }

        }

        setupCityDropdown()

    }

    private fun setupCityDropdown() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        binding.modifyAtvCity.setAdapter(adapter)

        binding.modifyAtvCity.setOnClickListener {
            val selectedCity = binding.modifyAtvCity.text.toString()
            val filteredCities = cities.filter { it != selectedCity }
            val filteredAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, filteredCities)
            binding.modifyAtvCity.setAdapter(filteredAdapter)
            binding.modifyAtvCity.dismissKeyboard {
                binding.modifyAtvCity.showDropDown()
            }
        }
    }

    private fun validateFields() {
        val city = binding.modifyAtvCity.text.toString().isNotEmpty()
        val address = binding.profileTieAddress.text.toString().isNotEmpty()
        val floor = binding.profileTieFloor.text.toString().isNotEmpty()
        val floorNumber = binding.profileTieFloorNumber.text.toString().isNotEmpty()

        if (city && address && floor && floorNumber) {
            binding.modifyDataBtn.isClickable = true
            binding.modifyDataBtn.setBackgroundResource(R.drawable.btn_gradient_red)
        } else {
            binding.modifyDataBtn.isClickable = false
            binding.modifyDataBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))
        }
    }

}
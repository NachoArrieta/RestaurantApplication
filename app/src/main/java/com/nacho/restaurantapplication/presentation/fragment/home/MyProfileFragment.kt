package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentMyProfileBinding
import com.nacho.restaurantapplication.domain.model.UserInformation
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

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
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupCityDropdown()
        setupSaveButton()

    }

    private fun setupObservers() {
        viewModel.userInformation.observe(viewLifecycleOwner) { userInformation ->
            userInformation?.let { user ->
                with(binding) {

                    val name = user.name.takeIf { !it.isNullOrEmpty() } ?: ""
                    val lastName = user.lastName.takeIf { !it.isNullOrEmpty() } ?: ""

                    profileTxtTitleName.text = getString(R.string.nav_header_name, name, lastName)
                    profileTxtTitleEmail.text = user.email.takeIf { !it.isNullOrEmpty() } ?: ""
                    profileTieName.setText(user.name.takeIf { !it.isNullOrEmpty() } ?: "")
                    profileTieLastname.setText(user.lastName.takeIf { !it.isNullOrEmpty() } ?: "")
                    profileTiePhone.setText(user.phone.takeIf { !it.isNullOrEmpty() } ?: "")
                    profileAtvCity.setText(user.city.takeIf { !it.isNullOrEmpty() } ?: "")
                    profileTieAddress.setText(user.address.takeIf { !it.isNullOrEmpty() } ?: "")
                    profileTieFloor.setText(user.floor.takeIf { !it.isNullOrEmpty() } ?: "")
                    profileTieFloorNumber.setText(user.number.takeIf { !it.isNullOrEmpty() } ?: "")

                }
            }
        }
    }

    private fun setupSaveButton() {
        binding.profileBtnSaveChanges.setOnClickListener {
            val uid = viewModel.userId.value
            val updatedUserInformation = UserInformation(
                name = binding.profileTieName.text.toString(),
                lastName = binding.profileTieLastname.text.toString(),
                phone = binding.profileTiePhone.text.toString(),
                city = binding.profileAtvCity.text.toString(),
                address = binding.profileTieAddress.text.toString(),
                floor = binding.profileTieFloor.text.toString(),
                number = binding.profileTieFloorNumber.text.toString()
            )

            if (!uid.isNullOrEmpty()) {
                viewModel.updateUserInformation(uid, updatedUserInformation)
                viewModel.fetchUserInformation(uid)
            } else {
                // Manejar el caso donde uid es nulo
            }

        }
    }

    private fun setupCityDropdown() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        binding.profileAtvCity.setAdapter(adapter)

        binding.profileAtvCity.setOnClickListener {
            val selectedCity = binding.profileAtvCity.text.toString()
            val filteredCities = cities.filter { it != selectedCity }
            val filteredAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, filteredCities)
            binding.profileAtvCity.setAdapter(filteredAdapter)
            binding.profileAtvCity.showDropDown()
        }
    }

}
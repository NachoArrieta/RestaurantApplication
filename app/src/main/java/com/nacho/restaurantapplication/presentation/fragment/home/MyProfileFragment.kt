package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.extensions.dismissKeyboard
import com.nacho.restaurantapplication.core.extensions.loseFocusAfterAction
import com.nacho.restaurantapplication.core.extensions.onTextChanged
import com.nacho.restaurantapplication.core.fragment.DialogAlertFragment
import com.nacho.restaurantapplication.databinding.FragmentMyProfileBinding
import com.nacho.restaurantapplication.domain.model.UserInformation
import com.nacho.restaurantapplication.presentation.fragment.home.state.MyProfileViewState
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var originalUserInformation: UserInformation
    private lateinit var updatedUserInformation: UserInformation

    private val cities by lazy {
        listOf(
            getString(R.string.profile_rio_tercero),
            getString(R.string.profile_almafuerte),
            getString(R.string.profile_villa_del_dique)
        )
    }

    private var alertDialog: DialogAlertFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()

    }

    private fun setupListeners() {
        with(binding) {
            profileTieName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            profileTieName.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            profileTieName.onTextChanged { onFieldChanged() }

            profileTieLastname.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            profileTieLastname.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            profileTieLastname.onTextChanged { onFieldChanged() }

            profileTiePhone.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            profileTiePhone.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            profileTiePhone.onTextChanged { onFieldChanged() }

            profileTieAddress.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            profileTieAddress.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            profileTieAddress.onTextChanged { onFieldChanged() }

            setupCityDropdown()

            profileBtnSaveChanges.setOnClickListener {
                validateChanges()
            }

        }
    }

    private fun setupObservers() {
        viewModel.userInformation.observe(viewLifecycleOwner) { userInformation ->
            userInformation?.let { user ->
                originalUserInformation = UserInformation(
                    name = user.name ?: "",
                    lastName = user.lastName ?: "",
                    phone = user.phone ?: "",
                    city = user.city ?: "",
                    address = user.address ?: "",
                    floor = user.floor ?: "",
                    number = user.number ?: ""
                )

                with(binding) {
                    val name = user.name.orEmpty()
                    val lastName = user.lastName.orEmpty()

                    profileTxtTitleName.text = getString(R.string.nav_header_name, name, lastName)
                    profileTxtTitleEmail.text = user.email.orEmpty()
                    profileTieName.setText(user.name.orEmpty())
                    profileTieLastname.setText(user.lastName.orEmpty())
                    profileTiePhone.setText(user.phone.orEmpty())
                    profileAtvCity.setText(user.city.orEmpty())
                    profileTieAddress.setText(user.address.orEmpty())
                    profileTieFloor.setText(user.floor.orEmpty())
                    profileTieFloorNumber.setText(user.number.orEmpty())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    updateUI(viewState)
                }
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        with(binding) {
            if (!hasFocus) {
                viewModel.onFieldsChanged(
                    UserInformation(
                        name = profileTieName.text.toString(),
                        lastName = profileTieLastname.text.toString(),
                        phone = profileTiePhone.text.toString(),
                        city = profileAtvCity.text.toString(),
                        address = profileTieAddress.text.toString(),
                        floor = profileTieFloor.text.toString(),
                        number = profileTieFloorNumber.text.toString(),
                    )
                )
            }
        }
    }

    private fun updateUI(viewState: MyProfileViewState) {
        with(binding) {
            profileTilName.error =
                if (viewState.isValidName) null else getString(R.string.signup_name_no_valid)
            profileTilLastname.error =
                if (viewState.isValidLastName) null else getString(R.string.signup_name_no_valid)
            profileTilPhone.error =
                if (viewState.isValidPhone) null else getString(R.string.profile_error_address)
            profileTilAddress.error =
                if (viewState.isValidAddress) null else getString(R.string.profile_error_address)
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
            binding.profileAtvCity.dismissKeyboard {
                binding.profileAtvCity.showDropDown()
            }
        }
    }

    private fun validateChanges() {
        updatedUserInformation = UserInformation(
            name = binding.profileTieName.text.toString(),
            lastName = binding.profileTieLastname.text.toString(),
            phone = binding.profileTiePhone.text.toString(),
            city = binding.profileAtvCity.text.toString(),
            address = binding.profileTieAddress.text.toString(),
            floor = binding.profileTieFloor.text.toString(),
            number = binding.profileTieFloorNumber.text.toString(),
        )

        if (originalUserInformation == updatedUserInformation) {
            showToast(getString(R.string.profile_no_changes))
        } else {
            val viewState = viewModel.viewState.value.infoUserValidated()
            if (!updatedUserInformation.isNotEmpty() || !viewState) {
                showToast(getString(R.string.signup_fields_no_valid))
            } else {
                showAlertDialog()
            }
        }
    }

    private fun showAlertDialog() {
        val uid = viewModel.userId.value

        alertDialog = DialogAlertFragment.newInstance(
            title = getString(R.string.dialog_return_profile_title),
            acceptButtonText = getString(R.string.dialog_accept),
            cancelButtonText = getString(R.string.cancel),
            onAcceptClick = {
                uid?.let { uid ->
                    viewModel.updateUserInformation(uid, updatedUserInformation)
                    viewModel.fetchUserInformation(uid)
                    showToast(getString(R.string.profile_changes_successful))
                }
                alertDialog?.dismiss()
            },
            onCancelClick = {
                alertDialog?.dismiss()
            }
        )

        alertDialog?.show(parentFragmentManager, "DialogAlertFragment")
    }
    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

}
package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.fragment.DialogAlertFragment
import com.nacho.restaurantapplication.core.utils.DatePickerHelper
import com.nacho.restaurantapplication.databinding.FragmentAddReservationBinding
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel

class AddReservationFragment : Fragment() {

    private var _binding: FragmentAddReservationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var datePickerHelper: DatePickerHelper
    private var alertDialog: DialogAlertFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupDatePicker()
        setupDropdowns()
        setupTextWatchers()
        setupConfirmButton()

    }

    private fun setupObservers() {
        viewModel.reservationValidateFields.observe(viewLifecycleOwner) { isValid ->
            binding.reservationsBtnConfirmReservation.apply {
                isClickable = isValid
                background = ContextCompat.getDrawable(
                    requireContext(),
                    if (isValid) R.drawable.btn_gradient_red else R.color.grey_dark
                )
            }
        }
    }

    private fun setupDatePicker() {
        datePickerHelper = DatePickerHelper(requireContext()) { selectedDate ->
            binding.reservationsAtDay.setText(selectedDate)
        }
        binding.reservationsAtDay.setOnClickListener {
            if (binding.reservationsAtCity.text.isNullOrEmpty()) {
                showToast(getString(R.string.reservations_select_city))
            } else {
                datePickerHelper.showDatePicker(binding.reservationsAtCity.text.toString())
            }
        }
    }

    private fun setupDropdowns() {
        setupDropdown(binding.reservationsAtCity, getCities())
        setupDropdown(binding.reservationsAtPlaces, getPlaces())
        setupDropdown(binding.reservationsAtHour, getHours())
    }

    private fun getCities(): List<String> = listOf(
        getString(R.string.profile_rio_tercero),
        getString(R.string.profile_villa_del_dique)
    )

    private fun getPlaces(): List<String> = listOf(
        getString(R.string.reservations_places_two),
        getString(R.string.reservations_places_four),
        getString(R.string.reservations_places_six),
        getString(R.string.reservations_places_eight),
        getString(R.string.reservations_places_more_eight)
    )

    private fun getHours(): List<String> = listOf(
        getString(R.string.reservations_hours_one),
        getString(R.string.reservations_hours_two),
        getString(R.string.reservations_hours_three),
        getString(R.string.reservations_hours_four),
        getString(R.string.reservations_hours_five),
        getString(R.string.reservations_hours_six),
        getString(R.string.reservations_hours_seven)
    )

    private fun setupTextWatchers() {
        val fields = listOf(
            binding.reservationsAtCity,
            binding.reservationsAtPlaces,
            binding.reservationsAtHour,
            binding.reservationsAtDay
        )
        fields.forEach { field ->
            field.addTextChangedListener {
                viewModel.checkFormValidity(
                    binding.reservationsAtCity.text.toString(),
                    binding.reservationsAtPlaces.text.toString(),
                    binding.reservationsAtHour.text.toString(),
                    binding.reservationsAtDay.text.toString()
                )
            }
        }
    }

    private fun setupDropdown(autoCompleteTextView: AutoCompleteTextView, options: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, options)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnClickListener {
            val selectedOption = autoCompleteTextView.text.toString()
            val filteredOptions = options.filter { it != selectedOption }
            val filteredAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, filteredOptions)
            autoCompleteTextView.setAdapter(filteredAdapter)
            autoCompleteTextView.showDropDown()
        }
    }

    private fun setupConfirmButton() {
        binding.reservationsBtnConfirmReservation.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val uid = viewModel.userId.value

        alertDialog = DialogAlertFragment.newInstance(
            title = getString(R.string.reservations_dialog),
            acceptButtonText = getString(R.string.dialog_accept),
            cancelButtonText = getString(R.string.cancel),
            onAcceptClick = {
                uid?.let {
                    viewModel.confirmReservation(
                        city = binding.reservationsAtCity.text.toString(),
                        day = binding.reservationsAtDay.text.toString(),
                        hour = binding.reservationsAtHour.text.toString(),
                        places = binding.reservationsAtPlaces.text.toString()
                    )
                    showToast(getString(R.string.reservations_ok))
                }
                alertDialog?.dismiss()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            },
            onCancelClick = {
                alertDialog?.dismiss()
            }
        )

        alertDialog?.show(parentFragmentManager, "DialogAlertFragment")
    }

    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

}
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
import com.nacho.restaurantapplication.core.utils.DatePickerHelper
import com.nacho.restaurantapplication.databinding.FragmentAddReservationBinding
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel

class AddReservationFragment : Fragment() {

    private var _binding: FragmentAddReservationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var datePickerHelper: DatePickerHelper

    private val cities by lazy {
        listOf(
            getString(R.string.profile_rio_tercero),
            getString(R.string.profile_villa_del_dique)
        )
    }

    private val places by lazy {
        listOf(
            getString(R.string.reservations_places_two),
            getString(R.string.reservations_places_four),
            getString(R.string.reservations_places_six),
            getString(R.string.reservations_places_eight),
            getString(R.string.reservations_places_more_eight)
        )
    }

    private val hours by lazy {
        listOf(
            getString(R.string.reservations_hours_one),
            getString(R.string.reservations_hours_two),
            getString(R.string.reservations_hours_three),
            getString(R.string.reservations_hours_four),
            getString(R.string.reservations_hours_five),
            getString(R.string.reservations_hours_six),
            getString(R.string.reservations_hours_seven)
        )
    }

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

        datePickerHelper = DatePickerHelper(requireContext()) { selectedDate ->
            binding.reservationsAtDay.setText(selectedDate)
        }

        with(binding) {
            setupDropdown(reservationsAtCity, cities)
            setupDropdown(reservationsAtPlaces, places)
            setupDropdown(reservationsAtHour, hours)

            reservationsAtDay.setOnClickListener {
                val selectedCity = reservationsAtCity.text.toString()
                if (selectedCity.isEmpty()) {
                    showToast(getString(R.string.reservations_select_city))
                } else {
                    datePickerHelper.showDatePicker(selectedCity)
                }
            }

            val fields = listOf(reservationsAtCity, reservationsAtPlaces, reservationsAtHour, reservationsAtDay)
            fields.forEach { field ->
                field.addTextChangedListener {
                    viewModel.checkFormValidity(
                        reservationsAtCity.text.toString(),
                        reservationsAtPlaces.text.toString(),
                        reservationsAtHour.text.toString(),
                        reservationsAtDay.text.toString()
                    )
                }
            }

        }
    }

    private fun setupObservers() {
        viewModel.reservationValidateFields.observe(viewLifecycleOwner) { isValid ->
            binding.reservationsBtnConfirmReservation.apply {
                isClickable = isValid
                background = if (isValid) {
                    ContextCompat.getDrawable(requireContext(), R.drawable.btn_gradient_red)
                } else {
                    ContextCompat.getDrawable(requireContext(), R.color.grey_dark)
                }
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

    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

}
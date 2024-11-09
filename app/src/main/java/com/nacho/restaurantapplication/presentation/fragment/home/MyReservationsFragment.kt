package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentMyReservationsBinding
import com.nacho.restaurantapplication.presentation.adapter.home.ReservationAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import androidx.lifecycle.lifecycleScope
import com.nacho.restaurantapplication.core.fragment.DialogAlertFragment
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MyReservationsFragment : Fragment() {

    private var _binding: FragmentMyReservationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var reservationAdapter: ReservationAdapter
    private var alertDialog: DialogAlertFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyReservationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = viewModel.userId.value
        if (uid != null) {
            viewModel.fetchUserReservations(uid)
        }

        setupObservers()

        with(binding) {
            reservationsBtnNewReservation.setOnClickListener {
                findNavController().navigate(R.id.action_nav_my_reservations_to_addReservationFragment)
            }
        }

    }

    private fun setupObservers() {
        viewModel.userReservations.observe(viewLifecycleOwner) { reservations ->
            with(binding) {
                reservationsLoading.visibility = View.VISIBLE
                reservationsRv.visibility = View.GONE
                reservationsCvEmpty.visibility = View.GONE
                reservationsBtnNewReservation.visibility = View.GONE

                if (reservations.isNullOrEmpty()) {
                    lifecycleScope.launch {
                        delay(3000)
                        reservationsLoading.visibility = View.GONE
                        reservationsRv.visibility = View.GONE
                        reservationsCvEmpty.visibility = View.VISIBLE
                        reservationsBtnNewReservation.visibility = View.VISIBLE
                    }
                } else {

                    val sortedReservations = reservations.sortedWith(compareBy(
                        { LocalDate.parse(it.day, DateTimeFormatter.ofPattern("d/MM/yyyy")) },
                        { LocalTime.parse(it.hour, DateTimeFormatter.ofPattern("H.mm 'hs'")) }
                    ))

                    lifecycleScope.launch {
                        delay(3000)
                        reservationAdapter = ReservationAdapter(
                            reservationList = sortedReservations,
                            onDeleteClick = { reservation ->
                                if (canDeleteReservation(reservation.day)) {
                                    showAlertDialog(reservation.reservationId)
                                } else {
                                    showToast(getString(R.string.reservations_not_delete))
                                }
                            }
                        )
                        reservationsRv.adapter = reservationAdapter

                        reservationsLoading.visibility = View.GONE
                        reservationsRv.visibility = View.VISIBLE
                        reservationsCvEmpty.visibility = View.GONE
                        reservationsBtnNewReservation.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun canDeleteReservation(reservationDay: String): Boolean {
        val reservationDate = LocalDate.parse(reservationDay, DateTimeFormatter.ofPattern("d/MM/yyyy"))
        val currentDate = LocalDate.now()
        return reservationDate.isAfter(currentDate)
    }

    private fun showAlertDialog(reservationId: String) {
        alertDialog = DialogAlertFragment.newInstance(
            title = getString(R.string.reservations_delete),
            acceptButtonText = getString(R.string.dialog_accept),
            cancelButtonText = getString(R.string.cancel),
            onAcceptClick = {
                viewModel.deleteUserReservation(reservationId)
                alertDialog?.dismiss()
                showToast(getString(R.string.reservations_delete_ok))
            },
            onCancelClick = {
                alertDialog?.dismiss()
            }
        )
        alertDialog?.show(parentFragmentManager, "DialogAlertFragment")
    }

    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

}
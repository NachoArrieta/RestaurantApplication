package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentMyReservationsBinding
import com.nacho.restaurantapplication.presentation.adapter.home.ReservationAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyReservationsFragment : Fragment() {

    private var _binding: FragmentMyReservationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var reservationAdapter: ReservationAdapter

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
                if (reservations.isNullOrEmpty()) {
                    reservationsLoading.visibility = View.GONE
                    reservationsRv.visibility = View.GONE
                    reservationsCvEmpty.visibility = View.VISIBLE
                } else {
                    reservationsLoading.visibility = View.VISIBLE
                    reservationsCvEmpty.visibility = View.GONE
                    reservationsRv.visibility = View.GONE
                    reservationsBtnNewReservation.apply {
                        isClickable = true
                        background = ContextCompat.getDrawable(
                            requireContext(), R.color.grey_dark
                        )
                    }

                    lifecycleScope.launch {
                        delay(2000)
                        reservationAdapter = ReservationAdapter(reservations) {}
                        reservationsRv.adapter = reservationAdapter

                        reservationsLoading.visibility = View.GONE
                        reservationsRv.visibility = View.VISIBLE
                        reservationsBtnNewReservation.apply {
                            isClickable = false
                            background = ContextCompat.getDrawable(
                                requireContext(), R.drawable.btn_gradient_red
                            )
                        }
                    }
                }
            }

        }
    }

}
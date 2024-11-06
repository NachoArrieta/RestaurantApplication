package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentMyReservationsBinding
import com.nacho.restaurantapplication.presentation.adapter.home.ReservationAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        setupObservers()

        with(binding) {
            reservationsBtnNewReservation.setOnClickListener {
                findNavController().navigate(R.id.action_nav_my_reservations_to_addReservationFragment)
            }
        }

    }

    private fun setupObservers() {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.userReservations.observe(viewLifecycleOwner) { reservations ->
                if (reservations.isNullOrEmpty()) {
                    binding.reservationsShimmer.visibility = View.GONE
                    binding.reservationsRv.visibility = View.GONE
                    binding.reservationsCvEmpty.visibility = View.VISIBLE
                } else {
                    binding.reservationsShimmer.visibility = View.GONE
                    binding.reservationsCvEmpty.visibility = View.GONE
                    binding.reservationsRv.visibility = View.VISIBLE
                    reservationAdapter = ReservationAdapter(reservations) {}
                    binding.reservationsRv.adapter = reservationAdapter
                }
            }
        }, 2000)
    }

}
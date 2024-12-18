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
import com.nacho.restaurantapplication.databinding.FragmentMyCouponsBinding
import com.nacho.restaurantapplication.presentation.adapter.home.CouponAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCouponsFragment : Fragment() {

    private var _binding: FragmentMyCouponsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var couponAdapter: CouponAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCouponsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = viewModel.userId.value
        if (user != null) {
            viewModel.fetchUserCoupons(user)
        }
        setupObservers()

        binding.couponsCvBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setupObservers() {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.userCoupons.observe(viewLifecycleOwner) { coupons ->
                if (coupons.isNullOrEmpty()) {
                    binding.reservationsLoading.visibility = View.GONE
                    binding.couponsRv.visibility = View.GONE
                    binding.couponsCv.visibility = View.VISIBLE
                } else {
                    binding.reservationsLoading.visibility = View.GONE
                    binding.couponsRv.visibility = View.VISIBLE
                    binding.couponsCv.visibility = View.GONE
                    couponAdapter = CouponAdapter(coupons) {}
                    binding.couponsRv.adapter = couponAdapter
                }
            }
        }, 2000)
    }

}
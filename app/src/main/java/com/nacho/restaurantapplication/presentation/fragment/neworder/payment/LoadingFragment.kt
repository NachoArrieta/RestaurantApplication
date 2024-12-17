package com.nacho.restaurantapplication.presentation.fragment.neworder.payment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentLoadingBinding
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PaymentMethodViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelOnBack()
        viewModel.setToolbarVisibility(false)
        setupObservers()
        setupListeners()

    }

    private fun setupListeners() {
        binding.loadingBtnOk.setOnClickListener {
            findNavController().popBackStack(R.id.nav_home, inclusive = false)
        }
    }

    private fun setupObservers() {
        viewModel.addCardSuccess.observe(viewLifecycleOwner) { isCardAdded ->
            if (isCardAdded) {
                binding.loadingProgressBar.visibility = View.VISIBLE
                binding.loadingTxtTitle.text = getString(R.string.add_payment_methods_wait_a_moment)
                binding.loadingTxtSubtitle.text = getString(R.string.add_payment_methods_card_validating)

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.loadingTxtSubtitle.visibility = View.GONE
                    binding.loadingStatusImg.visibility = View.VISIBLE
                    binding.loadingTxtTitle.text = getString(R.string.add_payment_methods_card_success)
                    binding.loadingBtnOk.visibility = View.VISIBLE
                }, 6000)
            }
        }
    }

    private fun cancelOnBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setToolbarVisibility(true)
        viewModel.reset()
    }

}
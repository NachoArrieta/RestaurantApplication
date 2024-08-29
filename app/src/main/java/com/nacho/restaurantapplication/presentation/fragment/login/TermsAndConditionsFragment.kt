package com.nacho.restaurantapplication.presentation.fragment.login

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textview.MaterialTextView
import com.nacho.restaurantapplication.databinding.FragmentTermsAndConditionsBinding
import com.nacho.restaurantapplication.presentation.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAndConditionsFragment : Fragment() {

    private var _binding: FragmentTermsAndConditionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            tacTxtTitleIntroduction.underline()
            tacTxtTitleUses.underline()
            tacTxtTitleRegister.underline()
            tacTxtTitlePayment.underline()
            tacTxtTitleReservation.underline()
            tacTxtTitleDeliveries.underline()
            tacTxtTitleReturns.underline()
            tacTxtTitlePrivacy.underline()
            tacTxtTitleIntellectual.underline()
            tacTxtTitleResponsibility.underline()
            tacTxtTitleModifyTerms.underline()
            tacTxtTitleContact.underline()

            tacBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            tacBtnAcept.setOnClickListener {
                viewModel.onTermsAccepted(true)
                findNavController().popBackStack()
            }

        }

    }

    private fun MaterialTextView.underline() {
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

}
package com.nacho.restaurantapplication.core.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentDialogAlertBinding


class DialogAlertFragment : DialogFragment() {

    private var _binding: FragmentDialogAlertBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            dialogBtnCancel.setOnClickListener {
                onDestroyView()
            }

            dialogBtnAccept.setOnClickListener {
                activity?.finish()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
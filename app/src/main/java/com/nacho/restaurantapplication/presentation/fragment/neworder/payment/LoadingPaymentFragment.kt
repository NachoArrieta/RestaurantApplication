package com.nacho.restaurantapplication.presentation.fragment.neworder.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentLoadingPaymentBinding
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoadingPaymentFragment : Fragment() {

    private var _binding: FragmentLoadingPaymentBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newOrderVM.setToolbarTitle(getString(R.string.toolbar_title_processing))
        newOrderVM.setOnBackVisibility(false)

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        val messages = listOf(
            "¿Sabías que todos los jueves tenemos promociones?",
            "Ahora, pedir desde tu app es mucho más fácil",
            "En la nueva sección de noticias, tenés toda nuestra información actualizada"
        )

        viewLifecycleOwner.lifecycleScope.launch {
            var index = 0
            repeat(3) {
                if (isAdded) {
                    binding.loadingPaymentTxtSubtitle.text = messages[index]
                    index = (index + 1) % messages.size
                    delay(2000)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(6000)
            if (isAdded) {
                findNavController().navigate(R.id.action_loadingPaymentFragment_to_statusFragment2)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


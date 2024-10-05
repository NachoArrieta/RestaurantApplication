package com.nacho.restaurantapplication.presentation.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nacho.restaurantapplication.databinding.FragmentHomeBinding
import com.nacho.restaurantapplication.presentation.activity.neworder.NewOrderActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            homeBtnGoNewOrder.setOnClickListener {
                goToNewOrder()
            }

        }

    }

    private fun goToNewOrder() {
        startActivity(Intent(context, NewOrderActivity::class.java))
        activity?.finish()
    }

}
package com.nacho.restaurantapplication.presentation.fragment.login

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentLoginBinding
import com.nacho.restaurantapplication.presentation.activity.home.HomeActivity
import com.nacho.restaurantapplication.presentation.viewmodel.login.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            loginBtnEntry.setOnClickListener {
                goToHome()
            }

            loginTxtCreateAccount.paintFlags = loginTxtCreateAccount.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            loginTxtCreateAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }

        }
    }

    private fun goToHome() {
        startActivity(Intent(context, HomeActivity::class.java))
        activity?.finish()
    }

}
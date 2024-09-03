package com.nacho.restaurantapplication.presentation.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.fragment.DialogAlertFragment
import com.nacho.restaurantapplication.databinding.FragmentVerifyEmailBinding
import com.nacho.restaurantapplication.presentation.activity.home.HomeActivity
import com.nacho.restaurantapplication.presentation.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerifyEmailFragment : Fragment() {

    private var _binding: FragmentVerifyEmailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    private var alertDialog: DialogAlertFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupObservers()
            verifyBtnGoToLogin.setOnClickListener {
                showAlertDialog()
            }
        }
    }

    private fun setupObservers() {
        viewModel.emailIsVerified.observe(viewLifecycleOwner) { isVerified ->
            if (isVerified) {
                lifecycleScope.launch {
                    setupUI()
                    delay(5000)
                    goToHome()
                }
            }
        }
    }

    private fun setupUI() {

        alertDialog?.let {
            if (it.isVisible) {
                it.dismiss()
            }
        }

        with(binding) {
            verifyLoading.visibility = View.INVISIBLE
            verifyBtnGoToLogin.visibility = View.GONE
            verifyPb.visibility = View.VISIBLE
            verifyTxtDescription.text = getString(R.string.verify_description_two)
        }

    }

    private fun goToHome() {
        startActivity(Intent(context, HomeActivity::class.java))
        activity?.finish()
    }

    private fun showAlertDialog() {
        alertDialog = DialogAlertFragment.newInstance(
            title = getString(R.string.dialog_return_login_title),
            acceptButtonText = getString(R.string.dialog_accept),
            cancelButtonText = getString(R.string.cancel),
            onAcceptClick = {
                parentFragmentManager.popBackStack()
            },
            onCancelClick = {
                alertDialog?.dismiss()
            }
        )
        alertDialog?.show(parentFragmentManager, "DialogAlertFragment")
    }
}
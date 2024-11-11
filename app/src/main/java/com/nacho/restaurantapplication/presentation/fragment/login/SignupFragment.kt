package com.nacho.restaurantapplication.presentation.fragment.login

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.extensions.loseFocusAfterAction
import com.nacho.restaurantapplication.core.extensions.onTextChanged
import com.nacho.restaurantapplication.databinding.FragmentSignupBinding
import com.nacho.restaurantapplication.presentation.model.UserSignup
import com.nacho.restaurantapplication.presentation.fragment.login.state.SignUpViewState
import com.nacho.restaurantapplication.presentation.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        with(binding) {
            signupTieName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            signupTieName.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            signupTieName.onTextChanged { onFieldChanged() }

            signupTieLastname.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            signupTieLastname.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            signupTieLastname.onTextChanged { onFieldChanged() }

            signupTiePhone.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            signupTiePhone.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            signupTiePhone.onTextChanged { onFieldChanged() }

            signupTieEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            signupTieEmail.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            signupTieEmail.onTextChanged { onFieldChanged() }

            signupTiePassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            signupTiePassword.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            signupTiePassword.onTextChanged { onFieldChanged() }

            signupTieConfirmPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            signupTieConfirmPassword.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            signupTieConfirmPassword.onTextChanged { onFieldChanged() }

            signupTxtGoLogin.paintFlags = signupTxtGoLogin.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            signupTxtTermsCondition.setOnClickListener {
                goToTermsAndConditions()
            }

            signupCbTermsCondition.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onTermsAccepted(isChecked)
            }

            signupTxtGoLogin.setOnClickListener {
                viewModel.onTermsAccepted(false)
                findNavController().popBackStack()
            }

            signupBtnRegister.setOnClickListener {
                validateAndRegister()
            }

        }
    }

    private fun setupObservers() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isTermsAccepted.observe(viewLifecycleOwner) { isAccepted ->
                    binding.signupCbTermsCondition.isChecked = isAccepted
                }

                viewModel.viewState.collect { viewState ->
                    updateUI(viewState)
                }

            }
        }

    }

    private fun updateUI(viewState: SignUpViewState) {
        with(binding) {
            signupTilName.error =
                if (viewState.isValidName) null else getString(R.string.signup_name_no_valid)
            signupTilLastname.error =
                if (viewState.isValidLastName) null else getString(R.string.signup_name_no_valid)
            signupTilPhone.error =
                if (viewState.isValidPhone) null else getString(R.string.signup_phone_no_valid)
            signupTilEmail.error =
                if (viewState.isValidEmail) null else getString(R.string.signup_email_no_valid)
            signupTilPassword.error =
                if (viewState.isValidPassword) null else getString(R.string.signup_password_no_valid)
            signupTilConfirmPassword.error =
                if (viewState.isValidConfirmPassword) null else getString(R.string.signup_confirm_password_no_valid)
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            viewModel.onFieldsChanged(
                UserSignup(
                    name = binding.signupTieName.text.toString(),
                    lastname = binding.signupTieLastname.text.toString(),
                    phone = binding.signupTiePhone.text.toString(),
                    email = binding.signupTieEmail.text.toString(),
                    password = binding.signupTiePassword.text.toString(),
                    confirmPassword = binding.signupTieConfirmPassword.text.toString(),
                )
            )
        }
    }

    private fun validateAndRegister() {
        val viewState = viewModel.viewState.value.userValidated()
        val termsAccepted = viewModel.isTermsAccepted.value ?: false
        val userSignup = UserSignup(
            name = binding.signupTieName.text.toString(),
            lastname = binding.signupTieLastname.text.toString(),
            phone = binding.signupTiePhone.text.toString(),
            email = binding.signupTieEmail.text.toString(),
            password = binding.signupTiePassword.text.toString(),
            confirmPassword = binding.signupTieConfirmPassword.text.toString(),
        )

        when {
            !userSignup.isNotEmpty() || !viewState -> showToast(getString(R.string.signup_fields_no_valid))
            !termsAccepted -> showToast(getString(R.string.signup_terms_no_valid))
            else -> {
                viewModel.checkEmailExists(userSignup.email) { exists ->
                    Log.d("SignupFragment", "Email exists: $exists for email: ${userSignup.email}")
                    if (exists) {
                        showToast(getString(R.string.signup_email_exist))
                    } else {
                        viewModel.createAccount(userSignup)
                        goToVerifyEmail()
                    }
                }
            }
        }
    }

    private fun goToTermsAndConditions() {
        findNavController().navigate(R.id.action_signupFragment_to_termsAndConditionsFragment)
    }

    private fun goToVerifyEmail() {
        findNavController().navigate(
            R.id.action_signupFragment_to_verifyEmailFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.signupFragment, true)
                .build()
        )
    }

    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onFieldsChanged(UserSignup())
        viewModel.onTermsAccepted(false)
    }

}
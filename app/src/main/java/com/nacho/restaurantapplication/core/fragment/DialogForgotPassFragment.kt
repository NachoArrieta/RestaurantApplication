package com.nacho.restaurantapplication.core.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.extensions.onTextChanged
import com.nacho.restaurantapplication.databinding.FragmentDialogForgotPassBinding
import com.nacho.restaurantapplication.presentation.viewmodel.login.LoginViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogForgotPassFragment : DialogFragment() {

    private var _binding: FragmentDialogForgotPassBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    private var emailValidationJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogForgotPassBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = dialog?.window?.attributes
        layoutParams?.gravity = Gravity.CENTER
        dialog?.window?.attributes = layoutParams

        val margin = resources.getDimensionPixelSize(R.dimen.dialog_horizontal_margin)

        val rootView = binding.root
        val params = rootView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(margin, 0, margin, 0)
        rootView.layoutParams = params
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            dialogBtnForgot.isEnabled = false
            binding.dialogBtnForgot.isClickable = false
        }

        setupObservers()
        setupListeners()

    }

    private fun setupListeners() {
        with(binding) {
            dialogTieEmail.onTextChanged { inputText ->
                val email = inputText.trim()

                emailValidationJob?.cancel()

                emailValidationJob = lifecycleScope.launch {
                    delay(300)

                    if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        viewModel.checkEmailExists(email) { exists ->
                            if (dialogTieEmail.text.toString().trim() == email) {
                                updateUI(email, exists)
                            }
                        }
                    } else {
                        updateUI(email, false)
                    }
                }
            }

            dialogBtnForgot.setOnClickListener {
                if (dialogBtnForgot.isEnabled) {
                    val email = dialogTieEmail.text.toString().trim()
                    viewModel.sendPasswordResetEmail(email)
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.resetPasswordResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (it) {
                    showToast(getString(R.string.dialog_reset_ok))
                    dismiss()
                } else {
                    showToast(getString(R.string.dialog_error_reset_failed))
                    dismiss()
                }
                viewModel.resetPasswordState()
            }
        }
    }

    private fun updateUI(email: String, exists: Boolean) {
        with(binding) {
            dialogTilEmail.error = when {
                email.isEmpty() -> null
                !exists -> getString(R.string.error_email_not_found)
                else -> null
            }

            dialogBtnForgot.isEnabled = exists
            dialogBtnForgot.isClickable = exists
            dialogBtnForgot.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (exists) R.color.red else R.color.grey
                )
            )
        }
    }

    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}




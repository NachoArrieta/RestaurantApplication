package com.nacho.restaurantapplication.core.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentDialogAlertBinding

class DialogAlertFragment : DialogFragment() {

    private var _binding: FragmentDialogAlertBinding? = null
    private val binding get() = _binding!!

    private var onAcceptClick: (() -> Unit)? = null
    private var onCancelClick: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAlertBinding.inflate(inflater, container, false)
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

        val title = arguments?.getString(ARG_TITLE)
        val acceptText = arguments?.getString(ARG_ACCEPT_TEXT)
        val cancelText = arguments?.getString(ARG_CANCEL_TEXT)

        with(binding) {
            dialogTxtTitle.text = title
            dialogBtnAccept.text = acceptText
            dialogBtnCancel.text = cancelText

            dialogBtnAccept.setOnClickListener {
                onAcceptClick?.invoke()
                dismiss()
                onDestroy()
            }

            dialogBtnCancel.setOnClickListener {
                onCancelClick?.invoke()
                dismiss()
                onDestroy()
            }
        }
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_ACCEPT_TEXT = "arg_accept_text"
        private const val ARG_CANCEL_TEXT = "arg_cancel_text"

        fun newInstance(
            title: String,
            acceptButtonText: String,
            cancelButtonText: String,
            onAcceptClick: () -> Unit,
            onCancelClick: () -> Unit
        ): DialogAlertFragment {
            return DialogAlertFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_ACCEPT_TEXT, acceptButtonText)
                    putString(ARG_CANCEL_TEXT, cancelButtonText)
                }
                this.onAcceptClick = onAcceptClick
                this.onCancelClick = onCancelClick
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
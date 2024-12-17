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
import com.bumptech.glide.Glide
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentDialogDeleteProductBinding

class DialogDeleteProductFragment : DialogFragment() {

    private var _binding: FragmentDialogDeleteProductBinding? = null
    private val binding get() = _binding!!

    private var onDeleteConfirmed: (() -> Unit)? = null
    private lateinit var productTitle: String
    private lateinit var productImageUrl: String
    private var productQuantity: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogDeleteProductBinding.inflate(inflater, container, false)
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

        productTitle = arguments?.getString(ARG_PRODUCT_TITLE) ?: ""
        productImageUrl = arguments?.getString(ARG_PRODUCT_IMAGE_URL) ?: ""
        productQuantity = arguments?.getInt(ARG_PRODUCT_QUANTITY) ?: 1

        with(binding) {
            dialogTxtTitleDeleteProduct.text = getString(
                R.string.dialog_delete_product_title,
                productQuantity,
                productTitle
            )

            Glide.with(requireContext())
                .load(productImageUrl)
                .into(dialogImgDeleteProduct)

            dialogBtnDeleteProduct.setOnClickListener {
                onDeleteConfirmed?.invoke()
                dismiss()
            }

            appCompatImageView3.setOnClickListener { dismiss() }

        }
    }

    companion object {
        private const val ARG_PRODUCT_TITLE = "arg_product_title"
        private const val ARG_PRODUCT_IMAGE_URL = "arg_product_image_url"
        private const val ARG_PRODUCT_QUANTITY = "arg_product_quantity"

        fun newInstance(
            productTitle: String,
            productImageUrl: String,
            productQuantity: Int,
            onDeleteConfirmed: () -> Unit
        ): DialogDeleteProductFragment {
            return DialogDeleteProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_TITLE, productTitle)
                    putString(ARG_PRODUCT_IMAGE_URL, productImageUrl)
                    putInt(ARG_PRODUCT_QUANTITY, productQuantity)
                }
                this.onDeleteConfirmed = onDeleteConfirmed
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
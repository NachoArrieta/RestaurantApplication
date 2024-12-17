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
import com.nacho.restaurantapplication.databinding.FragmentDialogAddProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogAddProductFragment : DialogFragment() {

    private var _binding: FragmentDialogAddProductBinding? = null
    private val binding get() = _binding!!

    private var onAddToCartClick: ((Int) -> Unit)? = null

    private lateinit var productTitle: String
    private lateinit var productDescription: String
    private lateinit var productImage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAddProductBinding.inflate(inflater, container, false)
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
        productDescription = arguments?.getString(ARG_PRODUCT_DESCRIPTION).toString()
        productImage = arguments?.getString(ARG_PRODUCT_IMAGE) ?: ""

        with(binding) {

            dialogTxtTitleProduct.text = productTitle


            if (productDescription.isEmpty()) {
                dialogTxtDescriptionProduct.visibility = View.GONE
            } else {
                dialogTxtDescriptionProduct.text = productDescription
                dialogTxtDescriptionProduct.visibility = View.VISIBLE
            }

            Glide.with(requireContext())
                .load(productImage)
                .into(binding.dialogImgProduct)

            var quantity = 1
            dialogTieQuantity.setText(quantity.toString())

            dialogBtnAdd.setOnClickListener {
                quantity++
                binding.dialogTieQuantity.setText(quantity.toString())
            }

            dialogBtnSubtract.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    binding.dialogTieQuantity.setText(quantity.toString())
                }
            }

            dialogBtnAddToCart.setOnClickListener {
                val quantityItem = binding.dialogTieQuantity.text.toString().toIntOrNull() ?: 1
                onAddToCartClick?.invoke(quantityItem)
                dismiss()
            }

            appCompatImageView3.setOnClickListener { dismiss() }

        }

    }

    companion object {
        private const val ARG_PRODUCT_TITLE = "arg_product_title"
        private const val ARG_PRODUCT_DESCRIPTION = "arg_product_description"
        private const val ARG_PRODUCT_IMAGE = "arg_product_image"

        fun newInstance(
            productTitle: String,
            productDescription: String,
            productImageUrl: String,
            onAddToCartClick: (Int) -> Unit
        ): DialogAddProductFragment {
            return DialogAddProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_TITLE, productTitle)
                    putString(ARG_PRODUCT_DESCRIPTION, productDescription)
                    putString(ARG_PRODUCT_IMAGE, productImageUrl)
                }
                this.onAddToCartClick = onAddToCartClick
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
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
import com.nacho.restaurantapplication.databinding.FragmentDialogAddAssembleBurgerBinding

class DialogAddAssembleBurgerFragment : DialogFragment() {

    private var _binding: FragmentDialogAddAssembleBurgerBinding? = null
    private val binding get() = _binding!!

    private var onAddToCartClick: ((Int) -> Unit)? = null

    private lateinit var productTitle: String
    private lateinit var productDescription: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAddAssembleBurgerBinding.inflate(inflater, container, false)
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

        with(binding) {
            dialogAssembleTxtTitleProduct.text = productTitle

            if (productDescription.isEmpty()) {
                dialogAssembleTxtDescriptionProduct.visibility = View.INVISIBLE
            } else {
                dialogAssembleTxtDescriptionProduct.text = productDescription
                dialogAssembleTxtDescriptionProduct.visibility = View.VISIBLE
            }

            dialogAssembleBtnAddToCart.setOnClickListener {
                val quantityItem = 1
                onAddToCartClick?.invoke(quantityItem)
                dismiss()
            }
        }
    }

    companion object {
        private const val ARG_PRODUCT_TITLE = "arg_product_title"
        private const val ARG_PRODUCT_DESCRIPTION = "arg_product_description"

        fun newInstance(
            productTitle: String,
            productDescription: String,
            onAddToCartClick: (Int) -> Unit
        ): DialogAddAssembleBurgerFragment {
            return DialogAddAssembleBurgerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_TITLE, productTitle)
                    putString(ARG_PRODUCT_DESCRIPTION, productDescription)
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
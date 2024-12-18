package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.fragment.DialogAddProductFragment
import com.nacho.restaurantapplication.data.model.CartItem
import com.nacho.restaurantapplication.data.model.Promotion
import com.nacho.restaurantapplication.databinding.FragmentPromotionsBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.PromotionAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromotionsFragment : Fragment() {

    private var _binding: FragmentPromotionsBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private lateinit var promotionList: PromotionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromotionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

    }

    private fun setupObservers() {

        newOrderVM.promotions.observe(viewLifecycleOwner) { promotions ->
            promotionList = PromotionAdapter(promotions) { promotion ->
                showDialogAddProduct(promotion)
            }
            binding.promotionsRv.adapter = promotionList
        }

        newOrderVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) {
                binding.promotionsRv.visibility = View.VISIBLE
                binding.promotionsShimmer.visibility = View.GONE
            }
        }

    }

    private fun showDialogAddProduct(promotion: Promotion) {
        val dialog = DialogAddProductFragment.newInstance(
            productTitle = promotion.title,
            productDescription = promotion.description,
            productImageUrl = promotion.image
        ) { quantity ->
            val cartItem = CartItem(
                title = promotion.title,
                image = promotion.image,
                description = promotion.description,
                type = "PROMOTION",
                quantity = quantity,
                price = promotion.price
            )
            newOrderVM.addToCart(cartItem)
            showToast(getString(R.string.dialog_add_product))
        }
        dialog.show(parentFragmentManager, DialogAddProductFragment::class.java.simpleName)
    }

    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

}
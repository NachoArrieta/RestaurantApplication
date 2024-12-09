package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.fragment.DialogDeleteProductFragment
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.CartItem
import com.nacho.restaurantapplication.databinding.ItemCartBinding

class ShoppingCartAdapter(
    private val cartItems: List<CartItem>,
    private val onDeleteItem: (CartItem) -> Unit
) : RecyclerView.Adapter<ShoppingCartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            with(binding) {
                itemCartTxtTitle.text = cartItem.title
                val description = cartItem.description
                itemCartTxtSubtitle.text = if (description.length > 120) {
                    "${description.take(120)}..."
                } else description
                updateTotalPrice(cartItem)
                dialogTieQuantity.setText(cartItem.quantity.toString())

                ImageLoader.loadImage(
                    itemView.context,
                    cartItem.image,
                    itemCartImg,
                    onLoadFailed = {},
                    onResourceReady = {}
                )

                dialogBtnAdd.setOnClickListener {
                    cartItem.quantity += 1
                    dialogTieQuantity.setText(cartItem.quantity.toString())
                    updateTotalPrice(cartItem)
                }

                dialogBtnSubtract.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        cartItem.quantity -= 1
                        dialogTieQuantity.setText(cartItem.quantity.toString())
                        updateTotalPrice(cartItem)
                    }
                }

                itemCartTxtDelete.setOnClickListener {
                    val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
                    DialogDeleteProductFragment.newInstance(
                        productTitle = cartItem.title,
                        productImageUrl = cartItem.image,
                        productQuantity = cartItem.quantity
                    ) {
                        onDeleteItem(cartItem)
                        notifyDataSetChanged()
                    }.show(fragmentManager, DialogDeleteProductFragment::class.java.simpleName)
                }
            }
        }

        private fun updateTotalPrice(cartItem: CartItem) {
            binding.itemCartTxtTotal.text = itemView.context.getString(
                R.string.price_format,
                cartItem.price * cartItem.quantity
            )
        }

    }
}
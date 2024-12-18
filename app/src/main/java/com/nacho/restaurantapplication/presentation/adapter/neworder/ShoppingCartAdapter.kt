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
    private val onDeleteItem: (CartItem) -> Unit,
    private val onQuantityChanged: (CartItem, Int) -> Unit
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

                // Verificamos si cartItem.image es "null" y en caso afirmativo usamos el icono por defecto
                val imageUrl = if (cartItem.image == "null") {
                    "android.resource://${itemView.context.packageName}/drawable/ic_example"
                } else {
                    cartItem.image
                }

                ImageLoader.loadImage(
                    itemView.context,
                    imageUrl,
                    itemCartImg,
                    onLoadFailed = {
                        itemCartImg.setImageResource(R.drawable.ic_example)
                    },
                    onResourceReady = {}
                )

                dialogBtnAdd.setOnClickListener {
                    val newQuantity = cartItem.quantity + 1
                    dialogTieQuantity.setText(newQuantity.toString())
                    updateTotalPrice(cartItem.copy(quantity = newQuantity))
                    onQuantityChanged(cartItem, newQuantity)
                }

                dialogBtnSubtract.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        val newQuantity = cartItem.quantity - 1
                        dialogTieQuantity.setText(newQuantity.toString())
                        updateTotalPrice(cartItem.copy(quantity = newQuantity))
                        onQuantityChanged(cartItem, newQuantity)
                    }
                }

                itemCartTxtDelete.setOnClickListener {
                    val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager

                    val imageUrl = if (cartItem.image == "null") {
                        "android.resource://${itemView.context.packageName}/drawable/ic_example"
                    } else {
                        cartItem.image
                    }

                    DialogDeleteProductFragment.newInstance(
                        productTitle = cartItem.title,
                        productImageUrl = imageUrl,
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
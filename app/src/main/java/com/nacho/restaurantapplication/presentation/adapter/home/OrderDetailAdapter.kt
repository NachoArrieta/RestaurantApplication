package com.nacho.restaurantapplication.presentation.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.CartItem
import com.nacho.restaurantapplication.databinding.ItemOrderDetailBinding

class OrderDetailAdapter(private val productList: List<CartItem>) :
    RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding = ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    class OrderDetailViewHolder(private val binding: ItemOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CartItem) {
            with(binding) {
                itemOrderDetailTitle.text = product.title
                itemOrderDetailQuantity.text = itemView.context.getString(R.string.orders_product_quantity, product.quantity)
            }
        }
    }
}
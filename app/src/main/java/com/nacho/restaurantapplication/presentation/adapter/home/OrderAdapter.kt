package com.nacho.restaurantapplication.presentation.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.Order
import com.nacho.restaurantapplication.databinding.ItemOrderBinding

class OrderAdapter(private val orderList: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orderList.size

    class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            with(binding) {
                itemOrderTxtDate.text = order.date
                itemOrderTxtTotal.text = itemView.context.getString(R.string.neworder_shopping_cart_total, order.totalInfo)

                val orderDetailAdapter = OrderDetailAdapter(order.productList)
                itemOrderRv.layoutManager = LinearLayoutManager(itemView.context)
                itemOrderRv.adapter = orderDetailAdapter

                itemOrderRv.visibility = View.GONE
                itemOrderImg.setImageResource(R.drawable.ic_arrow_down)

                root.setOnClickListener {
                    if (itemOrderRv.visibility == View.GONE) {
                        itemOrderRv.visibility = View.VISIBLE
                        itemOrderImg.setImageResource(R.drawable.ic_arrow_up)
                    } else {
                        itemOrderRv.visibility = View.GONE
                        itemOrderImg.setImageResource(R.drawable.ic_arrow_down)
                    }
                }
            }
        }
    }
}
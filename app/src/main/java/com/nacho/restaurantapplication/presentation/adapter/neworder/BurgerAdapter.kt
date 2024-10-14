package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Burger
import com.nacho.restaurantapplication.databinding.ItemProductBinding

class BurgerAdapter(
    private val burgerList: List<Burger>,
    private val onItemClick: (Burger) -> Unit
) : RecyclerView.Adapter<BurgerAdapter.BurgerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurgerViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BurgerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BurgerViewHolder, position: Int) {
        val burger = burgerList[position]
        holder.bind(burger)
        holder.itemView.setOnClickListener { onItemClick(burger) }
    }

    override fun getItemCount(): Int = burgerList.size

    class BurgerViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(burger: Burger) {
            with(binding) {
                itemProductTxtTitle.text = burger.title
                itemProductTxtPrice.text = itemView.context.getString(R.string.price_format, burger.price)
                ImageLoader.loadImage(
                    itemView.context,
                    burger.image,
                    itemProductImg,
                    onLoadFailed = {
                        // Manejar error al cargar imagen
                    },
                    onResourceReady = {}
                )
            }
        }
    }

}
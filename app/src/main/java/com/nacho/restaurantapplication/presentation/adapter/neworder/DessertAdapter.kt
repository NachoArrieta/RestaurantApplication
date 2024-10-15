package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Dessert
import com.nacho.restaurantapplication.databinding.ItemProductBinding

class DessertAdapter(
    private val dessertsList: List<Dessert>,
    private val onItemClick: (Dessert) -> Unit
) : RecyclerView.Adapter<DessertAdapter.DessertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DessertViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DessertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DessertViewHolder, position: Int) {
        val dessert = dessertsList[position]
        holder.bind(dessert)
        holder.itemView.setOnClickListener { onItemClick(dessert) }
    }

    override fun getItemCount(): Int = dessertsList.size

    class DessertViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dessert: Dessert) {
            with(binding) {
                itemProductTxtTitle.text = dessert.title
                itemProductTxtPrice.text = itemView.context.getString(R.string.price_format, dessert.price)
                ImageLoader.loadImage(
                    itemView.context,
                    dessert.image,
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
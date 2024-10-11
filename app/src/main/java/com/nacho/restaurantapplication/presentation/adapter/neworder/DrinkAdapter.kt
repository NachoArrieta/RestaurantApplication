package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Drink
import com.nacho.restaurantapplication.databinding.ItemProductBinding

class DrinkAdapter(
    private val drinksList: List<Drink>,
    private val onItemClick: (Drink) -> Unit
) : RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val drink = drinksList[position]
        holder.bind(drink)
        holder.itemView.setOnClickListener { onItemClick(drink) }
    }

    override fun getItemCount(): Int = drinksList.size

    class DrinkViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(drink: Drink) {
            with(binding) {
                itemProductTxtTitle.text = drink.title
                itemProductTxtPrice.text = drink.price.toString()
                ImageLoader.loadImage(
                    itemView.context,
                    drink.image,
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
package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Topping
import com.nacho.restaurantapplication.databinding.ItemToppingDressingBinding

class ToppingAdapter(
    private val toppingList: List<Topping>,
    private val onItemCheckedChange: (String, Boolean) -> Unit
) : RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToppingViewHolder {
        val binding = ItemToppingDressingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToppingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToppingViewHolder, position: Int) {
        val topping = toppingList[position]
        holder.bind(topping, selectedItems.contains(position))

        holder.binding.itemSizeCb.setOnCheckedChangeListener { _, isChecked ->
            updateSelection(position, isChecked)
            onItemCheckedChange(topping.title, isChecked)
        }

        holder.itemView.setOnClickListener {
            val isChecked = !holder.binding.itemSizeCb.isChecked
            holder.binding.itemSizeCb.isChecked = isChecked
            updateSelection(position, isChecked)
            onItemCheckedChange(topping.title, isChecked)
        }
    }

    override fun getItemCount(): Int = toppingList.size

    private fun updateSelection(position: Int, isChecked: Boolean) {
        if (isChecked) selectedItems.add(position)
        else selectedItems.remove(position)
    }

    class ToppingViewHolder(val binding: ItemToppingDressingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topping: Topping, isSelected: Boolean) {
            with(binding) {
                itemSizeTxtTitle.text = topping.title
                ImageLoader.loadImage(
                    itemView.context,
                    topping.image,
                    itemSizeImg,
                    onLoadFailed = {
                        // Manejar error al cargar la imagen
                    },
                    onResourceReady = {}
                )
                itemSizeCb.isChecked = isSelected
            }
        }
    }
}
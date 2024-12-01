package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Dressing
import com.nacho.restaurantapplication.databinding.ItemToppingDressingBinding

class DressingAdapter(
    private val dressingList: List<Dressing>,
    private val onItemClick: (Dressing) -> Unit
) : RecyclerView.Adapter<DressingAdapter.DressingViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DressingViewHolder {
        val binding = ItemToppingDressingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DressingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DressingViewHolder, position: Int) {
        val topping = dressingList[position]
        holder.bind(topping, selectedItems.contains(position))

        holder.itemView.setOnClickListener {
            toggleSelection(position)
            onItemClick(topping)
        }

        holder.binding.itemSizeCb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedItems.add(position) else selectedItems.remove(position)
        }
    }

    override fun getItemCount(): Int = dressingList.size

    private fun toggleSelection(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
    }

    class DressingViewHolder(val binding: ItemToppingDressingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dressing: Dressing, isSelected: Boolean) {
            with(binding) {
                itemSizeTxtTitle.text = dressing.title
                ImageLoader.loadImage(
                    itemView.context,
                    dressing.image,
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
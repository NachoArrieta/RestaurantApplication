package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Dressing
import com.nacho.restaurantapplication.databinding.ItemToppingDressingBinding

class DressingAdapter(
    private val dressingList: List<Dressing>,
    private val onItemCheckedChange: (String, Boolean) -> Unit
) : RecyclerView.Adapter<DressingAdapter.DressingViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DressingViewHolder {
        val binding = ItemToppingDressingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DressingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DressingViewHolder, position: Int) {
        val dressing = dressingList[position]
        holder.bind(dressing, selectedItems.contains(position))

        holder.binding.itemSizeCb.setOnCheckedChangeListener { _, isChecked ->
            updateSelection(position, isChecked)
            onItemCheckedChange(dressing.title, isChecked)
        }

        holder.itemView.setOnClickListener {
            val isChecked = !holder.binding.itemSizeCb.isChecked
            holder.binding.itemSizeCb.isChecked = isChecked
            updateSelection(position, isChecked)
            onItemCheckedChange(dressing.title, isChecked)
        }
    }

    override fun getItemCount(): Int = dressingList.size

    private fun updateSelection(position: Int, isChecked: Boolean) {
        if (isChecked) selectedItems.add(position)
        else selectedItems.remove(position)
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
package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.data.model.BurgerSize
import com.nacho.restaurantapplication.databinding.ItemSizeBinding

class SizeAdapter(
    private val sizeList: List<BurgerSize>,
    private val onItemClick: (BurgerSize) -> Unit
) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    private var selectedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val binding = ItemSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size = sizeList[position]
        val isSelected = selectedPosition == position
        holder.bind(size, isSelected)

        holder.itemView.setOnClickListener {
            handleItemClick(holder.adapterPosition)
        }

        holder.binding.itemSizeCb.setOnClickListener {
            handleItemClick(holder.adapterPosition)
        }
    }

    private fun handleItemClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position
            previousSelectedPosition?.let { notifyItemChanged(it) }
            notifyItemChanged(position)
            onItemClick(sizeList[position])
        }
    }

    override fun getItemCount(): Int = sizeList.size

    class SizeViewHolder(val binding: ItemSizeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(size: BurgerSize, isSelected: Boolean) {
            with(binding) {
                itemSizeTxtTitle.text = size.title
                itemSizeTxtSubtitle.text = size.subtitle
                itemSizeImg.setImageResource(size.image)
                itemSizeCb.isChecked = isSelected
            }
        }
    }

}
package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.databinding.ItemAssembleInformationBinding

class InformationBurgerAdapter(private var items: List<String>) :
    RecyclerView.Adapter<InformationBurgerAdapter.InformationBurgerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationBurgerViewHolder {
        val binding = ItemAssembleInformationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InformationBurgerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InformationBurgerViewHolder, position: Int) {
        holder.binding.itemAssembleTxt.text = items[position]
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<String>) {
        val diffCallback = InformationBurgerDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class InformationBurgerViewHolder(val binding: ItemAssembleInformationBinding) : RecyclerView.ViewHolder(binding.root)
}

class InformationBurgerDiffCallback(
    private val oldList: List<String>,
    private val newList: List<String>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
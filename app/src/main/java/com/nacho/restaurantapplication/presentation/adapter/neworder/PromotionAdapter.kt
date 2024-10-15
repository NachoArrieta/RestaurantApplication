package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Promotion
import com.nacho.restaurantapplication.databinding.ItemProductBinding

class PromotionAdapter(
    private val promotionsList: List<Promotion>,
    private val onItemClick: (Promotion) -> Unit
) : RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromotionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
        val promotion = promotionsList[position]
        holder.bind(promotion)
        holder.itemView.setOnClickListener { onItemClick(promotion) }
    }

    override fun getItemCount(): Int = promotionsList.size

    class PromotionViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(promotion: Promotion) {
            with(binding) {
                itemProductTxtTitle.text = promotion.title
                itemProductTxtPrice.text = itemView.context.getString(R.string.price_format, promotion.price)
                ImageLoader.loadImage(
                    itemView.context,
                    promotion.image,
                    itemProductImg
                )
            }
        }
    }

}
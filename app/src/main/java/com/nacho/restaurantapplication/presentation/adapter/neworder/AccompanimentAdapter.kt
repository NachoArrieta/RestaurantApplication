package com.nacho.restaurantapplication.presentation.adapter.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Accompaniment
import com.nacho.restaurantapplication.databinding.ItemProductBinding

class AccompanimentAdapter(
    private val accompanimentsList: List<Accompaniment>,
    private val onItemClick: (Accompaniment) -> Unit
) : RecyclerView.Adapter<AccompanimentAdapter.AccompanimentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccompanimentViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccompanimentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccompanimentViewHolder, position: Int) {
        val accompaniment = accompanimentsList[position]
        holder.bind(accompaniment)
        holder.itemView.setOnClickListener { onItemClick(accompaniment) }
    }

    override fun getItemCount(): Int = accompanimentsList.size

    class AccompanimentViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(accompaniment: Accompaniment) {
            with(binding) {
                itemProductTxtTitle.text = accompaniment.title
                itemProductTxtPrice.text = itemView.context.getString(R.string.price_format, accompaniment.price)
                ImageLoader.loadImage(
                    itemView.context,
                    accompaniment.image,
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
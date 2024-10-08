package com.nacho.restaurantapplication.presentation.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.Store
import com.nacho.restaurantapplication.databinding.ItemStoreBinding

class StoresAdapter(private val storeList: List<Store>) :
    RecyclerView.Adapter<StoresAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = storeList[position]
        holder.bind(store)
    }

    override fun getItemCount(): Int = storeList.size

    class StoreViewHolder(private val binding: ItemStoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(store: Store) {
            with(binding) {
                itemStoreTxtCity.text = store.city
                itemStoreTxtDays.text = store.days
                itemStoreTxtSchedules.text = store.schedule
                itemStoreTxtAddress.text = store.address
                itemStoreTxtPhone.text = store.phone

                ImageLoader.loadImage(
                    itemView.context,
                    store.image,
                    itemStoreImg,
                    onLoadFailed = {
                        itemStoreContentError.visibility = View.VISIBLE
                    },
                    onResourceReady = {
                        itemStoreContentError.visibility = View.GONE
                    }
                )

                itemStoreTxtShipmentsOnly.visibility = if (store.shipmentsOnly) View.VISIBLE else View.GONE

            }
        }
    }

}
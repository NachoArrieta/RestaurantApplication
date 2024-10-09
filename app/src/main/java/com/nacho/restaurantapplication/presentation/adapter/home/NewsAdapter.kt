package com.nacho.restaurantapplication.presentation.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.data.model.News
import com.nacho.restaurantapplication.databinding.ItemNewsBinding

class NewsAdapter(
    private val newsList: List<News>,
    private val onItemClick: (News) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)
        holder.itemView.setOnClickListener { onItemClick(news) }
    }

    override fun getItemCount(): Int = newsList.size

    class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            with(binding) {
                itemNewsTitle.text = news.title
                ImageLoader.loadImage(
                    itemView.context,
                    news.image,
                    itemNewsImg,
                    onLoadFailed = {
                        //Manejar error en caso de que no se pueda cargar la imagen
                    },
                    onResourceReady = {}
                )
            }
        }
    }

}
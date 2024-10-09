package com.nacho.restaurantapplication.data.network.news

import com.nacho.restaurantapplication.data.model.News

interface NewsRepository {
    suspend fun getNews(): List<News>
}
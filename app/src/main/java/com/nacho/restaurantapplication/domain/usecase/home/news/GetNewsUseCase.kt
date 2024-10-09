package com.nacho.restaurantapplication.domain.usecase.home.news

import com.nacho.restaurantapplication.data.model.News
import com.nacho.restaurantapplication.data.network.news.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): List<News> {
        return newsRepository.getNews()
    }
}
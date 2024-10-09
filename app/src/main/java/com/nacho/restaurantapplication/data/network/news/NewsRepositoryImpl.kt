package com.nacho.restaurantapplication.data.network.news

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.News
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : NewsRepository {

    override suspend fun getNews(): List<News> {
        val newsReference = firebaseDatabase.getReference("News")
        return try {
            val newsSnapshot = newsReference.get().await()
            newsSnapshot.children.mapNotNull { news ->
                val newsData = news.value as? Map<*, *> ?: return@mapNotNull null
                News(
                    title = newsData["Title"] as? String ?: "",
                    description = newsData["Description"] as? String ?: "",
                    image = newsData["Image"] as? String ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

}

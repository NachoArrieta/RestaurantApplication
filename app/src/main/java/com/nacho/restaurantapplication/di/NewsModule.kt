package com.nacho.restaurantapplication.di

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.network.news.NewsRepository
import com.nacho.restaurantapplication.data.network.news.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {
    @Provides
    @Singleton
    fun provideNewsRepository(firebaseDatabase: FirebaseDatabase): NewsRepository =
        NewsRepositoryImpl(firebaseDatabase)
}
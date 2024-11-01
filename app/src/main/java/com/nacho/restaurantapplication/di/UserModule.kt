package com.nacho.restaurantapplication.di

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.network.news.NewsRepository
import com.nacho.restaurantapplication.data.network.news.NewsRepositoryImpl
import com.nacho.restaurantapplication.data.network.user.UserInformationRepository
import com.nacho.restaurantapplication.data.network.user.UserInformationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideUserInformationRepository(firebaseDatabase: FirebaseDatabase): UserInformationRepository =
        UserInformationRepositoryImpl(firebaseDatabase)
}
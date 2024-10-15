package com.nacho.restaurantapplication.di

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.network.products.ProductsRepository
import com.nacho.restaurantapplication.data.network.products.ProductsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {
    @Provides
    @Singleton
    fun provideProductsRepository(firebaseDatabase: FirebaseDatabase): ProductsRepository =
        ProductsRepositoryImpl(firebaseDatabase)
}
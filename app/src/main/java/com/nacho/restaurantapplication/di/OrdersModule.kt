package com.nacho.restaurantapplication.di

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.network.orders.OrdersRepository
import com.nacho.restaurantapplication.data.network.orders.OrdersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrdersModule {
    @Provides
    @Singleton
    fun provideOrdersRepository(firebaseDatabase: FirebaseDatabase): OrdersRepository =
        OrdersRepositoryImpl(firebaseDatabase)
}
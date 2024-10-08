package com.nacho.restaurantapplication.di

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.network.stores.StoresRepository
import com.nacho.restaurantapplication.data.network.stores.StoresRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoresModule {
    @Provides
    @Singleton
    fun provideStoresRepository(firebaseDatabase: FirebaseDatabase): StoresRepository =
        StoresRepositoryImpl(firebaseDatabase)
}
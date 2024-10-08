package com.nacho.restaurantapplication.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.network.authentication.AuthenticationRepository
import com.nacho.restaurantapplication.data.network.authentication.AuthenticationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase
    ): AuthenticationRepository = AuthenticationRepositoryImpl(firebaseAuth, firebaseDatabase)
}
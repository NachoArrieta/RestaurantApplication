package com.nacho.restaurantapplication.di

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.network.paymentMethods.PaymentMethodsRepository
import com.nacho.restaurantapplication.data.network.paymentMethods.PaymentMethodsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentMethodsModule {
    @Provides
    @Singleton
    fun providePaymentMethodsRepository(firebaseDatabase: FirebaseDatabase): PaymentMethodsRepository =
        PaymentMethodsRepositoryImpl(firebaseDatabase)
}
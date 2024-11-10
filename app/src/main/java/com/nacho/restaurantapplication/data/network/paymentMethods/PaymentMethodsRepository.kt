package com.nacho.restaurantapplication.data.network.paymentMethods

import com.nacho.restaurantapplication.data.model.SupportedPaymentMethod

interface PaymentMethodsRepository {
    suspend fun getSupportedPaymentMethods(): List<SupportedPaymentMethod>
}
package com.nacho.restaurantapplication.data.network.paymentMethods

import com.nacho.restaurantapplication.data.model.Card
import com.nacho.restaurantapplication.data.model.SupportedPaymentMethod

interface PaymentMethodsRepository {
    suspend fun addCard(uid: String, card: Card): Boolean
    suspend fun getUserCards(uid: String): List<Card>
}
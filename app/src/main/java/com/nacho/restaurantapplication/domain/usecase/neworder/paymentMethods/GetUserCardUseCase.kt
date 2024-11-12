package com.nacho.restaurantapplication.domain.usecase.neworder.paymentMethods

import com.nacho.restaurantapplication.data.model.Card
import com.nacho.restaurantapplication.data.network.paymentMethods.PaymentMethodsRepository
import javax.inject.Inject

class GetUserCardUseCase @Inject constructor(
    private val paymentMethodsRepository: PaymentMethodsRepository
) {
    suspend operator fun invoke(uid: String): List<Card> {
        return paymentMethodsRepository.getUserCards(uid)
    }
}
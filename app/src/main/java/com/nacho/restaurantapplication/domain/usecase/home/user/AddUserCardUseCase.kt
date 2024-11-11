package com.nacho.restaurantapplication.domain.usecase.home.user

import com.nacho.restaurantapplication.data.network.paymentMethods.PaymentMethodsRepository
import com.nacho.restaurantapplication.domain.mapper.toCard
import com.nacho.restaurantapplication.domain.model.UserCard
import javax.inject.Inject

class AddUserCardUseCase @Inject constructor(
    private val paymentMethodsRepository: PaymentMethodsRepository
) {
    suspend operator fun invoke(uid: String, userCard: UserCard): Boolean {
        val card = userCard.toCard()
        return paymentMethodsRepository.addCard(uid, card)
    }
}
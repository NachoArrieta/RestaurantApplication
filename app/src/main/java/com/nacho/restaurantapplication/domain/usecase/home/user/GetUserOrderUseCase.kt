package com.nacho.restaurantapplication.domain.usecase.home.user

import com.nacho.restaurantapplication.data.model.Order
import com.nacho.restaurantapplication.data.network.user.UserInformationRepository
import javax.inject.Inject

class GetUserOrderUseCase @Inject constructor(
    private val userRepository: UserInformationRepository
) {
    suspend operator fun invoke(uid: String): List<Order> {
        return userRepository.getUserOrders(uid)
    }
}
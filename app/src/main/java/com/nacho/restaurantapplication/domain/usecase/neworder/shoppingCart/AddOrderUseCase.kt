package com.nacho.restaurantapplication.domain.usecase.neworder.shoppingCart

import com.nacho.restaurantapplication.data.model.Order
import com.nacho.restaurantapplication.data.network.orders.OrdersRepository
import javax.inject.Inject

class AddOrderUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository
) {
    suspend operator fun invoke(uid: String, order: Order): Boolean {
        return ordersRepository.addOrder(uid, order)
    }
}
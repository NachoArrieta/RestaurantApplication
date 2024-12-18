package com.nacho.restaurantapplication.data.network.orders

import com.nacho.restaurantapplication.data.model.Order

interface OrdersRepository {
    suspend fun addOrder(uid: String, order: Order): Boolean
}
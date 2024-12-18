package com.nacho.restaurantapplication.data.model

data class Order(
    val date: String = "",
    val hour: String = "",
    val productList: List<CartItem> = listOf(),
    val paymentInfo: Card = Card(),
    val shipmentInfo: DeliveryMethod = DeliveryMethod(),
    val discountInfo: Int,
    val totalInfo: Int
)
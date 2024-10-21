package com.nacho.restaurantapplication.data.model

data class User(
    val name: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val city: String? = "",
    val address: String? = "",
    val floor: String? = "",
    val number: String? = "",
    val coupons: List<Coupon>? = listOf(),
    val reservations: List<Reservation>? = listOf(),
    val orders: List<Order>? = listOf(),
    val paymentMethods: List<PaymentMethod>? = listOf()
)
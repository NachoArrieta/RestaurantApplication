package com.nacho.restaurantapplication.data.model

data class Order(
    val date: String = "",
    val hour: String = "",
    val burgers: List<Burger> = listOf(),
    val promotions: List<Promotion> = listOf(),
    val drinks: List<Drink> = listOf(),
    val accompaniments: List<Accompaniment> = listOf(),
    val desserts: List<Dessert> = listOf(),
    val discount: Coupon? = null,
    val total: Float = 0.0f,
)
package com.nacho.restaurantapplication.data.model

data class CartItem(
    val title: String = "",
    val description: String = "",
    val image: String = "",
    val type: String,
    var quantity: Int = 0,
    val price: Int = 0,
)
package com.nacho.restaurantapplication.data.model

data class Burger(
    val title: String = "",
    val description: String = "",
    val image: String = "",
    val size: String = "",
    val toppings: List<Topping> = listOf(),
    val dressings: List<Dressing> = listOf(),
    val price: Int = 0,
)
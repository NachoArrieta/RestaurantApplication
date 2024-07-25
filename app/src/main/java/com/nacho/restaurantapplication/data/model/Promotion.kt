package com.nacho.restaurantapplication.data.model

data class Promotion(
    val title: String = "",
    val description: String = "",
    val image: String = "",
    val burgers: List<Burger> = listOf(),
    val drinks: List<Drink> = listOf(),
    val accompaniments: List<Accompaniment> = listOf(),
    val price: String = "",
)
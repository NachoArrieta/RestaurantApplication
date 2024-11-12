package com.nacho.restaurantapplication.data.model

data class Card(
    val cardId: String = "",
    val cardNumber: String = "",
    val cardSince: String = "",
    val cardUntil: String = "",
    val cardName: String = "",
    val cardType: String = "",
    val cardBank: String = "",
    val cardCvv: String = "",
    val cardBrand: String = "",
    val cardAmount: Int = 0,
    val cardLimit: Int = 0
)
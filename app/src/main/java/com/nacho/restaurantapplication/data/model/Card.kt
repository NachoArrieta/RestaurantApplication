package com.nacho.restaurantapplication.data.model

data class Card(
    val cardNumber: String = "",
    val cardSince: String = "",
    val cardExpiration: String = "",
    val titularName: String = "",
    val cardType: String = "",
    val cardBank: String = "",
    val cardCVV: String = "",
    val cardBrand: String = "",
    val amount: String? = "",
    val totalLimit: String? = ""
)
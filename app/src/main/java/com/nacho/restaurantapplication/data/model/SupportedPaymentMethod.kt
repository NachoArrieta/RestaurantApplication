package com.nacho.restaurantapplication.data.model

data class SupportedPaymentMethod(
    val supportedCardNumber: String,
    val supportedCardBank: String,
    val supportedCardType: String,
    val supportedCardBrand: String
)
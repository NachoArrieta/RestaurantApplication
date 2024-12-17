package com.nacho.restaurantapplication.core.fragment.state

data class PaymentMethodViewState(
    val isValidCardNumber: Boolean = true,
    val isValidCardName: Boolean = true,
    val isValidCardSince: Boolean = true,
    val isValidCardUntil: Boolean = true,
    val isValidCardCvv: Boolean = true
)
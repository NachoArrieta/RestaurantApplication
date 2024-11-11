package com.nacho.restaurantapplication.domain.model

data class UserCard(
    val cardBank: String = "",
    val cardNumber: String = "",
    val cardName: String = "",
    val cardSince: String = "",
    val cardUntil: String = "",
    val cardType: String = "",
    val cardBrand: String = "",
    val cardCvv: String = "",
) {
    fun isNotEmpty() =
        cardBank.isNotEmpty() && cardNumber.isNotEmpty() && cardName.isNotEmpty()
                && cardSince.isNotEmpty() && cardUntil.isNotEmpty() && cardType.isNotEmpty()
                && cardBrand.isNotEmpty() && cardCvv.isNotEmpty()
}
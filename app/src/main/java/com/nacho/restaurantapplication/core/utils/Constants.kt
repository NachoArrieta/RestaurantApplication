package com.nacho.restaurantapplication.core.utils

object Constants {
    const val BURGERS = "Hamburguesas"
    const val PROMOTIONS = "Promociones"
    const val DRINKS = "Bebidas"
    const val DESSERTS = "Postres"
    const val ACCOMPANIMENTS = "Acompañamientos"

    const val MIN_NAME_LENGTH = 4
    const val MIN_PASSWORD_LENGTH = 8
    const val PHONE_LENGTH = 10
    const val ADDRESS_LENGTH = 6

    const val CARD_NUMBER_LENGTH = 22
    const val CARD_NAME_LENGTH = 8
    const val EXPIRATION_CARD_LENGTH = 5
    const val CVV_LENGTH = 3

    val SUPPORTED_CARD_NUMBERS = setOf(
        "4546  4200  0617  4342", // GaliciaCredito - Mastercard
        "4547  4400  0819  4546", // GaliciaDebito - Visa
        "4546  3900  1724  3131", // MacroCredito - Visa
        "4546  3900  0618  8484", // MacroDebito - Mastercard
        "4546  3700  3132  4400", // SantanderCredito - Visa
        "4546  3900  3354  5671"  // SantanderDebito - Visa
    )

}
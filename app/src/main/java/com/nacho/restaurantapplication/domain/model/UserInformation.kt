package com.nacho.restaurantapplication.domain.model

data class UserInformation(
    val name: String? = "",
    val lastName: String? = "",
    val phone: String? = "",
    val city: String? = "",
    val address: String? = "",
    val floor: String? = "",
    val number: String? = ""
)
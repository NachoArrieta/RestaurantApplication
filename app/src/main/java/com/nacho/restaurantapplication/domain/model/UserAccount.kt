package com.nacho.restaurantapplication.domain.model

data class UserAccount(
    val name: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val phone: String? = ""
)
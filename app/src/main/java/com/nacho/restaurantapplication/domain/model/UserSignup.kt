package com.nacho.restaurantapplication.domain.model

data class UserSignup(
    val name: String = "",
    val lastname: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
) {
    fun isNotEmpty() =
        name.isNotEmpty() && lastname.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
}
package com.nacho.restaurantapplication.presentation.fragment.login.state

data class SignUpViewState(
    val isLoading: Boolean = false,
    val isValidName: Boolean = true,
    val isValidLastName: Boolean = true,
    val isValidPhone: Boolean = true,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true,
    val isValidConfirmPassword: Boolean = true
) {
    fun userValidated() =
        isValidName && isValidLastName && isValidPhone && isValidEmail && isValidPassword && isValidConfirmPassword
}


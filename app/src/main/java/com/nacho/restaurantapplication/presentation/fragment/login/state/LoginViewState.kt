package com.nacho.restaurantapplication.presentation.fragment.login.state

data class LoginViewState(
    val isLoading: Boolean = false,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true
)
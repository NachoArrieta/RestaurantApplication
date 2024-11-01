package com.nacho.restaurantapplication.presentation.fragment.home.state

data class MyProfileViewState(
    val isLoading: Boolean = false,
    val isValidName: Boolean = true,
    val isValidLastName: Boolean = true,
    val isValidPhone: Boolean = true,
    val isValidCity: Boolean = true,
    val isValidAddress: Boolean = true,
    val isValidFloor: Boolean = true,
    val isValidNumber: Boolean = true
) {
    fun infoUserValidated() = isValidName && isValidLastName && isValidPhone && isValidCity && isValidAddress && isValidFloor && isValidNumber
}

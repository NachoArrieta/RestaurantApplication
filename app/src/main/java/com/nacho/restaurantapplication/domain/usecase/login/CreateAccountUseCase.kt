package com.nacho.restaurantapplication.domain.usecase.login

import com.google.firebase.auth.AuthResult
import com.nacho.restaurantapplication.data.network.AuthenticationRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult? =
        repository.createAccount(email, password)
}
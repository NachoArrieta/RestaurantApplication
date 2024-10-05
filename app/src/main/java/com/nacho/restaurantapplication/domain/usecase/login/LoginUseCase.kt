package com.nacho.restaurantapplication.domain.usecase.login

import com.google.firebase.auth.AuthResult
import com.nacho.restaurantapplication.data.network.AuthenticationRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult? {
        return repository.loginUser(email, password)
    }
}
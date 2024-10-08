package com.nacho.restaurantapplication.domain.usecase.login

import com.nacho.restaurantapplication.data.network.authentication.AuthenticationRepository
import javax.inject.Inject

class CheckEmailExistsUseCase @Inject constructor(
    private val authRepository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return authRepository.checkEmailExists(email)
    }
}
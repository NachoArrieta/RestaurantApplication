package com.nacho.restaurantapplication.domain.usecase.login

import com.nacho.restaurantapplication.data.network.authentication.AuthenticationRepository
import javax.inject.Inject

class SendEmailVerificationUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(): Boolean = repository.sendVerificationEmail()
}
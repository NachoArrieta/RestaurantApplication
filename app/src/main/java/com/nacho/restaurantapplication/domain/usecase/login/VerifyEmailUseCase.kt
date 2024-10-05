package com.nacho.restaurantapplication.domain.usecase.login

import com.nacho.restaurantapplication.data.network.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.emailIsVerified()
}
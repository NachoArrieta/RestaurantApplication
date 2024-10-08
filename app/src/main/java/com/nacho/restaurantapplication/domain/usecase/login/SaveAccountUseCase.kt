package com.nacho.restaurantapplication.domain.usecase.login

import com.nacho.restaurantapplication.data.network.authentication.AuthenticationRepository
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {

}
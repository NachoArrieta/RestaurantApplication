package com.nacho.restaurantapplication.domain.usecase.login

import com.nacho.restaurantapplication.data.network.AuthenticationRepository
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    //suspend operator fun invoke(user: UserRegister): Boolean = repository.saveUser(user.toUser())
}
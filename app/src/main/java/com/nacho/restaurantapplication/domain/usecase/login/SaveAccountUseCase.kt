package com.nacho.restaurantapplication.domain.usecase.login

import com.nacho.restaurantapplication.data.network.authentication.AuthenticationRepository
import com.nacho.restaurantapplication.domain.mapper.toData
import com.nacho.restaurantapplication.domain.model.UserAccount
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(
    private val repository: AuthenticationRepository,
) {
    suspend operator fun invoke(user: UserAccount, uid: String): Boolean {
        val userData = user.toData()
        return repository.saveUser(userData, uid)
    }
}
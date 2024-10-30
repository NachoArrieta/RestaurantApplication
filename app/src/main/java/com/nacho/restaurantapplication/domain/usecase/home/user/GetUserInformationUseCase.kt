package com.nacho.restaurantapplication.domain.usecase.home.user

import com.nacho.restaurantapplication.data.model.User
import com.nacho.restaurantapplication.data.network.user.UserInformationRepository
import javax.inject.Inject

class GetUserInformationUseCase @Inject constructor(
    private val userRepository: UserInformationRepository
) {
    suspend operator fun invoke(uid: String): User? {
        return userRepository.getUserInformation(uid)
    }
}
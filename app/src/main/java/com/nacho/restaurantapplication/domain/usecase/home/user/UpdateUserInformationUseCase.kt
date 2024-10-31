package com.nacho.restaurantapplication.domain.usecase.home.user

import com.nacho.restaurantapplication.data.network.user.UserInformationRepository
import com.nacho.restaurantapplication.domain.mapper.toUser
import com.nacho.restaurantapplication.domain.model.UserInformation
import javax.inject.Inject

class UpdateUserInformationUseCase @Inject constructor(
    private val userRepository: UserInformationRepository
) {
    suspend operator fun invoke(uid: String, userInformation: UserInformation): Boolean {
        val user = userInformation.toUser()
        return userRepository.updateUserInformation(uid, user)
    }
}
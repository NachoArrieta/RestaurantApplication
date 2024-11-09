package com.nacho.restaurantapplication.domain.usecase.home.user

import com.nacho.restaurantapplication.data.network.user.UserInformationRepository
import javax.inject.Inject

class DeleteUserReservationUseCase @Inject constructor(
    private val userRepository: UserInformationRepository
) {
    suspend operator fun invoke(uid: String, reservationId: String): Boolean {
        return userRepository.deleteUserReservation(uid, reservationId)
    }
}
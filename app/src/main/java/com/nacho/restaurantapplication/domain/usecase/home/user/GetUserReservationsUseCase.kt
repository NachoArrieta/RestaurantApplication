package com.nacho.restaurantapplication.domain.usecase.home.user

import com.nacho.restaurantapplication.data.model.Reservation
import com.nacho.restaurantapplication.data.network.user.UserInformationRepository
import javax.inject.Inject

class GetUserReservationsUseCase @Inject constructor(
    private val userRepository: UserInformationRepository
) {
    suspend operator fun invoke(uid: String): List<Reservation> {
        return userRepository.getUserReservations(uid)
    }
}
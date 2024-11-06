package com.nacho.restaurantapplication.domain.usecase.home.user

import com.nacho.restaurantapplication.data.network.user.UserInformationRepository
import com.nacho.restaurantapplication.domain.mapper.toReservation
import com.nacho.restaurantapplication.domain.model.ReservationInformation
import javax.inject.Inject

class AddReservationUseCase @Inject constructor(
    private val userInformationRepository: UserInformationRepository
) {
    suspend operator fun invoke(uid: String, reservationInformation: ReservationInformation): Boolean {
        val reservation = reservationInformation.toReservation()
        return userInformationRepository.addReservation(uid, reservation)
    }
}
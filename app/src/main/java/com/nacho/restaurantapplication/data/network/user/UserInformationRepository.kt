package com.nacho.restaurantapplication.data.network.user

import com.nacho.restaurantapplication.data.model.Reservation
import com.nacho.restaurantapplication.data.model.User

interface UserInformationRepository {
    suspend fun getUserInformation(uid: String): User?
    suspend fun updateUserInformation(uid: String, user: User): Boolean
    suspend fun addReservation(uid: String, reservation: Reservation): Boolean
}
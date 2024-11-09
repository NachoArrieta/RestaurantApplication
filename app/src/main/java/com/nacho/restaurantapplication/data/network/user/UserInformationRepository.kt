package com.nacho.restaurantapplication.data.network.user

import com.nacho.restaurantapplication.data.model.Coupon
import com.nacho.restaurantapplication.data.model.Reservation
import com.nacho.restaurantapplication.data.model.User

interface UserInformationRepository {
    suspend fun getUserInformation(uid: String): User?
    suspend fun updateUserInformation(uid: String, user: User): Boolean
    suspend fun getUserCoupons(uid: String): List<Coupon>
    suspend fun getUserReservations(uid: String): List<Reservation>
    suspend fun addReservation(uid: String, reservation: Reservation): Boolean
    suspend fun deleteUserReservation(uid: String, reservationId: String): Boolean
}
package com.nacho.restaurantapplication.data.network.user

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.Coupon
import com.nacho.restaurantapplication.data.model.Reservation
import com.nacho.restaurantapplication.data.model.User
import kotlinx.coroutines.tasks.await

class UserInformationRepositoryImpl(private val firebaseDatabase: FirebaseDatabase) : UserInformationRepository {

    override suspend fun getUserInformation(uid: String): User? {
        val userReference = firebaseDatabase.getReference("Users/$uid")
        return try {
            val userSnapshot = userReference.get().await()
            val userData = userSnapshot.value as? Map<*, *> ?: return null
            val user = User(
                name = userData["Name"] as? String ?: "",
                lastName = userData["LastName"] as? String ?: "",
                email = userData["Email"] as? String ?: "",
                phone = userData["Phone"] as? String ?: "",
                city = userData["City"] as? String ?: "",
                address = userData["Address"] as? String ?: "",
                floor = userData["Floor"] as? String ?: "",
                number = userData["Number"] as? String ?: "",
                coupons = (userData["Coupons"] as? Map<*, *>)?.map { entry ->
                    val couponData = entry.value as? Map<*, *>
                    Coupon(
                        title = couponData?.get("Title") as? String ?: "",
                        description = couponData?.get("Description") as? String ?: "",
                        code = couponData?.get("Code") as? String ?: "",
                        percentage = (couponData?.get("percentage") as? String)?.toIntOrNull() ?: 0,
                        expirationDate = couponData?.get("ExpirationDate") as? String ?: "",
                        amount = (couponData?.get("Amount") as? String)?.toIntOrNull() ?: 0
                    )
                } ?: emptyList(),
                reservations = (userData["Reservations"] as? Map<*, *>)?.map { entry ->
                    val reservationData = entry.value as? Map<*, *>
                    Reservation(
                        city = reservationData?.get("City") as? String ?: "",
                        day = reservationData?.get("Day") as? String ?: "",
                        hour = reservationData?.get("Hour") as? String ?: "",
                        places = reservationData?.get("Places") as? String ?: ""
                    )
                } ?: emptyList()
            )
            user
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateUserInformation(uid: String, user: User): Boolean {
        val userReference = firebaseDatabase.getReference("Users/$uid")
        return try {
            val userMap = mapOf(
                "Name" to user.name,
                "LastName" to user.lastName,
                "Phone" to user.phone,
                "City" to user.city,
                "Address" to user.address,
                "Floor" to user.floor,
                "Number" to user.number
            )

            userReference.updateChildren(userMap).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addReservation(uid: String, reservation: Reservation): Boolean {
        val userReservationsReference = firebaseDatabase.getReference("Users/$uid/Reservations")
        val reservationKey = userReservationsReference.push().key ?: return false

        val reservationMap = mapOf(
            "City" to reservation.city,
            "Day" to reservation.day,
            "Hour" to reservation.hour,
            "Places" to reservation.places
        )

        return try {
            userReservationsReference.child(reservationKey).setValue(reservationMap).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}
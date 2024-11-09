package com.nacho.restaurantapplication.data.network.user

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
            User(
                name = userData["Name"] as? String ?: "",
                lastName = userData["LastName"] as? String ?: "",
                email = userData["Email"] as? String ?: "",
                phone = userData["Phone"] as? String ?: "",
                city = userData["City"] as? String ?: "",
                address = userData["Address"] as? String ?: "",
                floor = userData["Floor"] as? String ?: "",
                number = userData["Number"] as? String ?: ""
            )
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

    override suspend fun getUserCoupons(uid: String): List<Coupon> {
        val couponsReference = firebaseDatabase.getReference("Users/$uid/Coupons")
        return try {
            val couponsSnapshot = couponsReference.get().await()
            (couponsSnapshot.value as? Map<*, *>)?.map { entry ->
                val couponData = entry.value as? Map<*, *>
                Coupon(
                    title = couponData?.get("Title") as? String ?: "",
                    description = couponData?.get("Description") as? String ?: "",
                    code = couponData?.get("Code") as? String ?: "",
                    percentage = (couponData?.get("percentage") as? String)?.toIntOrNull() ?: 0,
                    expirationDate = couponData?.get("ExpirationDate") as? String ?: "",
                    amount = (couponData?.get("Amount") as? String)?.toIntOrNull() ?: 0
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getUserReservations(uid: String): List<Reservation> {
        val reservationsReference = firebaseDatabase.getReference("Users/$uid/Reservations")
        return try {
            val reservationsSnapshot = reservationsReference.get().await()
            (reservationsSnapshot.value as? Map<*, *>)?.map { entry ->
                val reservationData = entry.value as? Map<*, *>
                Reservation(
                    reservationId = entry.key.toString(),
                    city = reservationData?.get("City") as? String ?: "",
                    day = reservationData?.get("Day") as? String ?: "",
                    hour = reservationData?.get("Hour") as? String ?: "",
                    places = reservationData?.get("Places") as? String ?: ""
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
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

    override suspend fun deleteUserReservation(uid: String, reservationId: String): Boolean {
        val reservationReference = firebaseDatabase.getReference("Users/$uid/Reservations/$reservationId")
        return try {
            reservationReference.removeValue().await()
            true
        } catch (e: Exception) {
            false
        }
    }

}
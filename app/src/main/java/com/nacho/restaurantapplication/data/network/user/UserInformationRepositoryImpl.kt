package com.nacho.restaurantapplication.data.network.user

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.Card
import com.nacho.restaurantapplication.data.model.CartItem
import com.nacho.restaurantapplication.data.model.Coupon
import com.nacho.restaurantapplication.data.model.DeliveryMethod
import com.nacho.restaurantapplication.data.model.Order
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
                    expirationDate = couponData?.get("ExpirationDate") as? String ?: "",
                    amount = (couponData?.get("Amount") as? Number)?.toInt() ?: 0
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

    override suspend fun getUserOrders(uid: String): List<Order> {
        val ordersReference = firebaseDatabase.getReference("Users/$uid/Orders")
        return try {
            val ordersSnapshot = ordersReference.get().await()
            (ordersSnapshot.value as? Map<*, *>)?.map { entry ->
                val orderData = entry.value as? Map<*, *>
                Order(
                    date = orderData?.get("Date") as? String ?: "",
                    hour = orderData?.get("Hour") as? String ?: "",
                    productList = (orderData?.get("ProductList") as? Map<*, *>)?.map { productEntry ->
                        val productData = productEntry.value as? Map<*, *>
                        CartItem(
                            title = productData?.get("Title") as? String ?: "",
                            description = productData?.get("Description") as? String ?: "",
                            image = productData?.get("Image") as? String ?: "",
                            price = (productData?.get("Price") as? Number)?.toInt() ?: 0,
                            quantity = (productData?.get("Quantity") as? Number)?.toInt() ?: 0,
                            type = productData?.get("Type") as? String ?: ""
                        )
                    } ?: emptyList(),
                    paymentInfo = (orderData?.get("PaymentInfo") as? Map<*, *>)?.let { paymentData ->
                        Card(
                            cardNumber = paymentData["CardNumber"] as? String ?: "",
                            cardName = paymentData["CardName"] as? String ?: "",
                            cardBank = paymentData["CardBank"] as? String ?: "",
                            cardType = paymentData["CardType"] as? String ?: "",
                            cardBrand = paymentData["CardBrand"] as? String ?: "",
                            cardUntil = paymentData["CardUntil"] as? String ?: "",
                            cardCvv = paymentData["CardCvv"] as? String ?: ""
                        )
                    } ?: Card(),
                    shipmentInfo = (orderData?.get("ShipmentInfo") as? Map<*, *>)?.let { shipmentData ->
                        DeliveryMethod(
                            address = shipmentData["Address"] as? String ?: "",
                            city = shipmentData["City"] as? String ?: "",
                            floor = shipmentData["Floor"] as? String ?: "",
                            number = shipmentData["Number"] as? String ?: "",
                            shippingPrice = (shipmentData["ShippingPrice"] as? Number)?.toInt() ?: 0,
                            type = shipmentData["Type"] as? String ?: ""
                        )
                    } ?: DeliveryMethod(),
                    discountInfo = (orderData?.get("DiscountInfo") as? Number)?.toInt() ?: 0,
                    totalInfo = (orderData?.get("TotalInfo") as? Number)?.toInt() ?: 0
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

}
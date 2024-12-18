package com.nacho.restaurantapplication.data.network.orders

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.Order
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : OrdersRepository {

    override suspend fun addOrder(uid: String, order: Order): Boolean {
        val userOrdersReference = firebaseDatabase.getReference("Users/$uid/Orders")
        val orderKey = userOrdersReference.push().key ?: return false
        val orderMap = mapOf(
            "Date" to order.date,
            "Hour" to order.hour,
            "ProductList" to order.productList.associate {
                it.title to mapOf(
                    "Title" to it.title,
                    "Description" to it.description,
                    "Image" to it.image,
                    "Type" to it.type,
                    "Quantity" to it.quantity,
                    "Price" to it.price
                )
            },
            "PaymentInfo" to mapOf(
                "CardBank" to order.paymentInfo.cardBank,
                "CardBrand" to order.paymentInfo.cardBrand,
                "CardCvv" to order.paymentInfo.cardCvv,
                "CardName" to order.paymentInfo.cardName,
                "CardNumber" to order.paymentInfo.cardNumber,
                "CardSince" to order.paymentInfo.cardSince,
                "CardType" to order.paymentInfo.cardType,
                "CardUntil" to order.paymentInfo.cardUntil
            ),
            "ShipmentInfo" to mapOf(
                "Type" to order.shipmentInfo.type,
                "ShippingPrice" to order.shipmentInfo.shippingPrice,
                "Address" to order.shipmentInfo.address,
                "Floor" to order.shipmentInfo.floor,
                "Number" to order.shipmentInfo.number,
                "City" to order.shipmentInfo.city
            ),
            "DiscountInfo" to order.discountInfo,
            "TotalInfo" to order.totalInfo
        )

        return try {
            userOrdersReference.child(orderKey).setValue(orderMap).await()
            true
        } catch (e: Exception) {
            false
        }

    }
}
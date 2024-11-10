package com.nacho.restaurantapplication.data.network.paymentMethods

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.SupportedPaymentMethod
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PaymentMethodsRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : PaymentMethodsRepository {

    override suspend fun getSupportedPaymentMethods(): List<SupportedPaymentMethod> {
        val paymentMethodsReference = firebaseDatabase.getReference("SupportedPaymentTypes")
        return try {
            val paymentMethodsSnapshot = paymentMethodsReference.get().await()
            paymentMethodsSnapshot.children.mapNotNull { paymentMethod ->
                val methodData = paymentMethod.value as? Map<*, *> ?: return@mapNotNull null
                SupportedPaymentMethod(
                    supportedCardNumber = methodData["SupportedCardNumber"].toString(),
                    supportedCardBank = methodData["SupportedCardBank"].toString(),
                    supportedCardType = methodData["SupportedCardType"].toString(),
                    supportedCardBrand = methodData["SupportedCardBrand"].toString()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

}
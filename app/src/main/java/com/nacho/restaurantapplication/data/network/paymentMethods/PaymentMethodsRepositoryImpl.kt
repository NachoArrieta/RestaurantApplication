package com.nacho.restaurantapplication.data.network.paymentMethods

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.Card
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

    override suspend fun addCard(uid: String, card: Card): Boolean {
        val userCardsReference = firebaseDatabase.getReference("Users/$uid/Cards")
        val cardKey = userCardsReference.push().key ?: return false
        val cardMap = mapOf(
            "CardBank" to card.cardBank,
            "CardNumber" to card.cardNumber,
            "CardSince" to card.cardSince,
            "CardUntil" to card.cardUntil,
            "CardName" to card.cardName,
            "CardType" to card.cardType,
            "CardBrand" to card.cardBrand,
            "CardCvv" to card.cardCvv
        )
        return try {
            userCardsReference.child(cardKey).setValue(cardMap).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}
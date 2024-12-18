package com.nacho.restaurantapplication.data.network.paymentMethods

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.Card
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PaymentMethodsRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : PaymentMethodsRepository {

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

    override suspend fun getUserCards(uid: String): List<Card> {
        val cardsReference = firebaseDatabase.getReference("Users/$uid/Cards")
        return try {
            val cardsSnapshot = cardsReference.get().await()
            (cardsSnapshot.value as? Map<*, *>)?.map { entry ->
                val cardData = entry.value as? Map<*, *>
                Card(
                    cardId = entry.key.toString(),
                    cardBank = cardData?.get("CardBank") as? String ?: "",
                    cardBrand = cardData?.get("CardBrand") as? String ?: "",
                    cardCvv = cardData?.get("CardCvv") as? String ?: "",
                    cardName = cardData?.get("CardName") as? String ?: "",
                    cardNumber = cardData?.get("CardNumber") as? String ?: "",
                    cardSince = cardData?.get("CardSince") as? String ?: "",
                    cardType = cardData?.get("CardType") as? String ?: "",
                    cardUntil = cardData?.get("CardUntil") as? String ?: "")
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

}
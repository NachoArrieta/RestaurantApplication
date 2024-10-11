package com.nacho.restaurantapplication.data.network.products

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.Drink
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ProductsRepository {

    override suspend fun getDrinks(): Map<String, List<Drink>> {
        val drinksReference = firebaseDatabase.getReference("Products/Drinks")
        return try {
            val drinksSnapshot = drinksReference.get().await()
            val categories = mutableMapOf<String, List<Drink>>()

            drinksSnapshot.children.forEach { category ->
                val drinksList = category.children.mapNotNull { drink ->
                    val drinkData = drink.value as? Map<*, *> ?: return@mapNotNull null
                    Drink(
                        title = drinkData["Title"] as? String ?: "",
                        image = drinkData["Image"] as? String ?: "",
                        price = (drinkData["Price"] as? String) ?: ""
                    )
                }
                categories[category.key ?: ""] = drinksList
            }
            categories
        } catch (e: Exception) {
            emptyMap()
        }
    }

}
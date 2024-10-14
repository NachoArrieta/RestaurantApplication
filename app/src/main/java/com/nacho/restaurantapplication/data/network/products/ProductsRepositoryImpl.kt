package com.nacho.restaurantapplication.data.network.products

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.Accompaniment
import com.nacho.restaurantapplication.data.model.Burger
import com.nacho.restaurantapplication.data.model.Dessert
import com.nacho.restaurantapplication.data.model.Drink
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ProductsRepository {

    override suspend fun getBurgers(): Map<String, List<Burger>> {
        val burgersReference = firebaseDatabase.getReference("Products/Burgers")
        return try {
            val burgersSnapshot = burgersReference.get().await()
            val categories = mutableMapOf<String, List<Burger>>()

            burgersSnapshot.children.forEach { category ->
                val burgersList = category.children.mapNotNull { burger ->
                    val burgerData = burger.value as? Map<*, *> ?: return@mapNotNull null
                    Burger(
                        title = burgerData["Title"] as? String ?: "",
                        description = burgerData["Description"] as? String ?: "",
                        image = burgerData["Image"] as? String ?: "",
                        size = burgerData["Size"] as? String ?: "",
                        price = when (val priceValue = burgerData["Price"]) {
                            is Int -> priceValue
                            is Long -> priceValue.toInt()
                            is Double -> priceValue.toInt()
                            else -> 0
                        }
                    )
                }
                categories[category.key ?: ""] = burgersList
            }
            categories
        } catch (e: Exception) {
            emptyMap()
        }
    }

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
                        price = when (val priceValue = drinkData["Price"]) {
                            is Int -> priceValue
                            is Long -> priceValue.toInt()
                            is Double -> priceValue.toInt()
                            else -> 0
                        }
                    )
                }
                categories[category.key ?: ""] = drinksList
            }
            categories
        } catch (e: Exception) {
            emptyMap()
        }
    }

    override suspend fun getDesserts(): Map<String, List<Dessert>> {
        val dessertsReference = firebaseDatabase.getReference("Products/Desserts")
        return try {
            val dessertsSnapshot = dessertsReference.get().await()
            val categories = mutableMapOf<String, List<Dessert>>()

            dessertsSnapshot.children.forEach { category ->
                val dessertsList = category.children.mapNotNull { dessert ->
                    val dessertData = dessert.value as? Map<*, *> ?: return@mapNotNull null
                    Dessert(
                        title = dessertData["Title"] as? String ?: "",
                        image = dessertData["Image"] as? String ?: "",
                        price = when (val priceValue = dessertData["Price"]) {
                            is Int -> priceValue
                            is Long -> priceValue.toInt()
                            is Double -> priceValue.toInt()
                            else -> 0
                        }
                    )
                }
                categories[category.key ?: ""] = dessertsList
            }
            categories
        } catch (e: Exception) {
            emptyMap()
        }
    }

    override suspend fun getAccompaniments(): List<Accompaniment> {
        val accompanimentsReference = firebaseDatabase.getReference("Products/Accompaniments")
        return try {
            val accompanimentsSnapshot = accompanimentsReference.get().await()
            accompanimentsSnapshot.children.mapNotNull { accompaniment ->
                val accompanimentData = accompaniment.value as? Map<*, *> ?: return@mapNotNull null
                Accompaniment(
                    title = accompanimentData["Title"] as? String ?: "",
                    image = accompanimentData["Image"] as? String ?: "",
                    description = accompanimentData["Description"] as? String ?: "",
                    price = when (val priceValue = accompanimentData["Price"]) {
                        is Int -> priceValue
                        is Long -> priceValue.toInt()
                        is Double -> priceValue.toInt()
                        else -> 0
                    }
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

}
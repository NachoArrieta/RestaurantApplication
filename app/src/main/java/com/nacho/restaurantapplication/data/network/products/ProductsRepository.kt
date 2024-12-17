package com.nacho.restaurantapplication.data.network.products

import com.nacho.restaurantapplication.data.model.Accompaniment
import com.nacho.restaurantapplication.data.model.Burger
import com.nacho.restaurantapplication.data.model.Dessert
import com.nacho.restaurantapplication.data.model.Dressing
import com.nacho.restaurantapplication.data.model.Drink
import com.nacho.restaurantapplication.data.model.Promotion
import com.nacho.restaurantapplication.data.model.Topping

interface ProductsRepository {
    suspend fun getBurgers(): Map<String, List<Burger>>
    suspend fun getPromotions(): List<Promotion>
    suspend fun getDrinks(): Map<String, List<Drink>>
    suspend fun getDesserts(): Map<String, List<Dessert>>
    suspend fun getAccompaniments(): List<Accompaniment>
    suspend fun getToppings(): List<Topping>
    suspend fun getDressings(): List<Dressing>
}


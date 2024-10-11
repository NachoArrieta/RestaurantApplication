package com.nacho.restaurantapplication.data.network.products

import com.nacho.restaurantapplication.data.model.Accompaniment
import com.nacho.restaurantapplication.data.model.Drink

interface ProductsRepository {
    suspend fun getDrinks(): Map<String, List<Drink>>
    suspend fun getAccompaniments(): List<Accompaniment>
}


package com.nacho.restaurantapplication.data.network.stores

import com.nacho.restaurantapplication.data.model.Store

interface StoresRepository {
    suspend fun getStores(): List<Store>
}
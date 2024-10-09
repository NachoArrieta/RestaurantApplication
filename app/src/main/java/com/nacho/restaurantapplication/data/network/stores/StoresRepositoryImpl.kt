package com.nacho.restaurantapplication.data.network.stores

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.Store
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StoresRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : StoresRepository {

    override suspend fun getStores(): List<Store> {
        val storesReference = firebaseDatabase.getReference("Stores")
        return try {
            val storesSnapshot = storesReference.get().await()
            storesSnapshot.children.mapNotNull { store ->
                val storeData = store.value as? Map<*, *> ?: return@mapNotNull null
                Store(
                    city = storeData["City"] as? String ?: "",
                    address = storeData["Address"] as? String ?: "",
                    schedule = storeData["Schedules"] as? String ?: "",
                    days = storeData["Days"] as? String ?: "",
                    image = storeData["Image"] as? String ?: "",
                    phone = storeData["Phone"] as? String ?: "",
                    shipmentsOnly = storeData["ShipmentOnly"] as? Boolean ?: false
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

}
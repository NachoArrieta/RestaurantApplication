package com.nacho.restaurantapplication.domain.usecase.home.stores

import com.nacho.restaurantapplication.data.model.Store
import com.nacho.restaurantapplication.data.network.stores.StoresRepository
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(
    private val storesRepository: StoresRepository
) {
    suspend operator fun invoke(): List<Store> {
        return storesRepository.getStores()
    }
}
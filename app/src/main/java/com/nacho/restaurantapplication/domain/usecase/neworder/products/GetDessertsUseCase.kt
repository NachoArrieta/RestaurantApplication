package com.nacho.restaurantapplication.domain.usecase.neworder.products

import com.nacho.restaurantapplication.data.model.Dessert
import com.nacho.restaurantapplication.data.network.products.ProductsRepository
import javax.inject.Inject

class GetDessertsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): Map<String, List<Dessert>> {
        return productsRepository.getDesserts()
    }
}
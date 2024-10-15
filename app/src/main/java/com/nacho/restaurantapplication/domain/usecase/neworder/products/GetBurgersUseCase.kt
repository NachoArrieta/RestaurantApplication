package com.nacho.restaurantapplication.domain.usecase.neworder.products

import com.nacho.restaurantapplication.data.model.Burger
import com.nacho.restaurantapplication.data.network.products.ProductsRepository
import javax.inject.Inject

class GetBurgersUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): Map<String, List<Burger>> {
        return productsRepository.getBurgers()
    }
}
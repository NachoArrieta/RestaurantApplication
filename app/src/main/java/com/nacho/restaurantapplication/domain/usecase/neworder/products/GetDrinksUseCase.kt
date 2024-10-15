package com.nacho.restaurantapplication.domain.usecase.neworder.products

import com.nacho.restaurantapplication.data.model.Drink
import com.nacho.restaurantapplication.data.network.products.ProductsRepository
import javax.inject.Inject

class GetDrinksUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): Map<String, List<Drink>> {
        return productsRepository.getDrinks()
    }
}
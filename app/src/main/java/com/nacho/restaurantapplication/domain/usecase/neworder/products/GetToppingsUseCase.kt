package com.nacho.restaurantapplication.domain.usecase.neworder.products

import com.nacho.restaurantapplication.data.model.Topping
import com.nacho.restaurantapplication.data.network.products.ProductsRepository
import javax.inject.Inject

class GetToppingsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): List<Topping> {
        return productsRepository.getToppings()
    }
}
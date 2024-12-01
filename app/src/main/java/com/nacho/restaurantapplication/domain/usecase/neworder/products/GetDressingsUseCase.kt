package com.nacho.restaurantapplication.domain.usecase.neworder.products

import com.nacho.restaurantapplication.data.model.Dressing
import com.nacho.restaurantapplication.data.network.products.ProductsRepository
import javax.inject.Inject

class GetDressingsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): List<Dressing> {
        return productsRepository.getDressings()
    }
}
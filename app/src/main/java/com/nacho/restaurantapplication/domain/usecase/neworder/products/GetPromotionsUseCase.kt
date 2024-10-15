package com.nacho.restaurantapplication.domain.usecase.neworder.products

import com.nacho.restaurantapplication.data.model.Promotion
import com.nacho.restaurantapplication.data.network.products.ProductsRepository
import javax.inject.Inject

class GetPromotionsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): List<Promotion> {
        return productsRepository.getPromotions()
    }
}
package com.nacho.restaurantapplication.domain.usecase.neworder.products

import com.nacho.restaurantapplication.data.model.Accompaniment
import com.nacho.restaurantapplication.data.network.products.ProductsRepository
import javax.inject.Inject

class GetAccompanimentsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(): List<Accompaniment> {
        return productsRepository.getAccompaniments()
    }
}
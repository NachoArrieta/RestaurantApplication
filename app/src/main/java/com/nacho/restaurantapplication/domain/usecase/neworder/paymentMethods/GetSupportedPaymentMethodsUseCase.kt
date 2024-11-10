package com.nacho.restaurantapplication.domain.usecase.neworder.paymentMethods

import com.nacho.restaurantapplication.data.model.SupportedPaymentMethod
import com.nacho.restaurantapplication.data.network.paymentMethods.PaymentMethodsRepository
import javax.inject.Inject

class GetSupportedPaymentMethodsUseCase @Inject constructor(
    private val paymentRepository: PaymentMethodsRepository
) {
    suspend operator fun invoke(): List<SupportedPaymentMethod> {
        return paymentRepository.getSupportedPaymentMethods()
    }
}
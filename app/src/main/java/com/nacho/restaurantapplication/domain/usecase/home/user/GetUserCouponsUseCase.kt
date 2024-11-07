package com.nacho.restaurantapplication.domain.usecase.home.user

import com.nacho.restaurantapplication.data.model.Coupon
import com.nacho.restaurantapplication.data.network.user.UserInformationRepository
import javax.inject.Inject

class GetUserCouponsUseCase @Inject constructor(
    private val userRepository: UserInformationRepository
) {
    suspend operator fun invoke(uid: String): List<Coupon> {
        return userRepository.getUserCoupons(uid)
    }
}
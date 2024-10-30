package com.nacho.restaurantapplication.data.network.user

import com.nacho.restaurantapplication.data.model.User
import com.nacho.restaurantapplication.domain.model.UserInformation

interface UserInformationRepository {
    suspend fun getUserInformation(uid: String): User?
}
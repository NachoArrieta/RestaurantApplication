package com.nacho.restaurantapplication.data.network.user

import com.nacho.restaurantapplication.data.model.User

interface UserInformationRepository {
    suspend fun getUserInformation(uid: String): User?
    suspend fun updateUserInformation(uid: String, user: User): Boolean
}
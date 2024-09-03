package com.nacho.restaurantapplication.data.network

import com.google.firebase.auth.AuthResult
import com.nacho.restaurantapplication.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun createAccount(email: String, password: String): AuthResult?
    suspend fun sendVerificationEmail(): Boolean
    suspend fun checkEmailExists(email: String): Boolean
    suspend fun saveUser(user: User): Boolean
    fun emailIsVerified(): Flow<Boolean>
}
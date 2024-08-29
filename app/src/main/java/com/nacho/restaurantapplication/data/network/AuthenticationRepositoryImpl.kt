package com.nacho.restaurantapplication.data.network

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : AuthenticationRepository {

    override suspend fun createAccount(email: String, password: String): AuthResult? {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            authResult
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun sendVerificationEmail(): Boolean {
        return try {
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun emailIsVerified(): Flow<Boolean> = flow {
        while (true) {
            firebaseAuth.currentUser?.reload()?.await()
            emit(firebaseAuth.currentUser?.isEmailVerified ?: false)
            delay(2000)
        }
    }

    override suspend fun saveUser(user: User): Boolean {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return false
            firebaseDatabase.reference.child("users").child(userId).setValue(user).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}
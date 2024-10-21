package com.nacho.restaurantapplication.data.network.authentication

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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

    override suspend fun saveUser(user: User, uid: String): Boolean {
        return try {
            val userMap = mapOf(
                "Name" to user.name,
                "LastName" to user.lastName,
                "Email" to user.email,
                "Phone" to user.phone,
                "City" to user.city,
                "Address" to user.address,
                "Floor" to user.floor,
                "Number" to user.number
            )

            firebaseDatabase.reference.child("Users").child(uid).setValue(userMap).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun checkEmailExists(email: String): Boolean {
        return try {
            // Creamos un usuario temporal con una contraseña aleatoria
            val fakePassword = "userAdmin"
            firebaseAuth.createUserWithEmailAndPassword(email.trim(), fakePassword).await()
            firebaseAuth.currentUser?.delete()?.await()
            // Si llega aquí, significa que el email no existía
            false
        } catch (e: FirebaseAuthUserCollisionException) {
            // Si llegamos a la excepcion, significa que el email ya existe
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun loginUser(email: String, password: String): AuthResult? {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult
        } catch (e: Exception) {
            null
        }
    }

}
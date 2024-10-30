package com.nacho.restaurantapplication.data.network.user

import com.google.firebase.database.FirebaseDatabase
import com.nacho.restaurantapplication.data.model.User
import kotlinx.coroutines.tasks.await

class UserInformationRepositoryImpl(private val firebaseDatabase: FirebaseDatabase) : UserInformationRepository {

    override suspend fun getUserInformation(uid: String): User? {
        val userReference = firebaseDatabase.getReference("Users/$uid")
        return try {
            val userSnapshot = userReference.get().await()
            val userData = userSnapshot.value as? Map<*, *> ?: return null

            val user = User(
                name = userData["Name"] as? String ?: "",
                lastName = userData["LastName"] as? String ?: "",
                email = userData["Email"] as? String ?: "",
                phone = userData["Phone"] as? String ?: "",
                city = userData["City"] as? String ?: "",
                address = userData["Address"] as? String ?: "",
                floor = userData["Floor"] as? String ?: "",
                number = userData["Number"] as? String ?: ""
            )

            user
        } catch (e: Exception) {
            null
        }
    }

}
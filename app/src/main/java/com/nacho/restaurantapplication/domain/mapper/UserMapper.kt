package com.nacho.restaurantapplication.domain.mapper

import com.nacho.restaurantapplication.data.model.User
import com.nacho.restaurantapplication.domain.model.UserAccount

fun UserAccount.toData(): User {
    return User(
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        phone = this.phone
    )
}
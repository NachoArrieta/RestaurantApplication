package com.nacho.restaurantapplication.domain.mapper

import com.nacho.restaurantapplication.data.model.User
import com.nacho.restaurantapplication.domain.model.UserAccount
import com.nacho.restaurantapplication.domain.model.UserInformation

fun UserAccount.toData(): User {
    return User(
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        phone = this.phone
    )
}

fun User.toUserInformation(): UserInformation {
    return UserInformation(
        name = this.name,
        lastName = this.lastName,
        phone = this.phone,
        email = this.email,
        city = this.city,
        address = this.address,
        floor = this.floor,
        number = this.number
    )
}
fun UserInformation.toUser(): User {
    return User(
        name = this.name,
        lastName = this.lastName,
        phone = this.phone,
        email = this.email,
        city = this.city,
        address = this.address,
        floor = this.floor,
        number = this.number
    )
}
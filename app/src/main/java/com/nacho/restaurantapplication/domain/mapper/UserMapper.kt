package com.nacho.restaurantapplication.domain.mapper

import com.nacho.restaurantapplication.data.model.Card
import com.nacho.restaurantapplication.data.model.Reservation
import com.nacho.restaurantapplication.data.model.User
import com.nacho.restaurantapplication.domain.model.ReservationInformation
import com.nacho.restaurantapplication.domain.model.UserAccount
import com.nacho.restaurantapplication.domain.model.UserCard
import com.nacho.restaurantapplication.domain.model.UserInformation

fun UserAccount.toData(): User {
    return User(
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        phone = this.phone
    )
}

fun UserInformation.toUser(): User {
    return User(
        name = this.name,
        lastName = this.lastName,
        phone = this.phone,
        city = this.city,
        address = this.address,
        floor = this.floor,
        number = this.number
    )
}

fun ReservationInformation.toReservation(): Reservation {
    return Reservation(
        city = this.city,
        day = this.day,
        hour = this.hour,
        places = this.places
    )
}

fun UserCard.toCard(): Card {
    return Card(
        cardBank = this.cardBank,
        cardNumber = this.cardNumber,
        cardSince = this.cardSince,
        cardUntil = this.cardUntil,
        cardName = this.cardName,
        cardType = this.cardType,
        cardBrand = this.cardBrand,
        cardCvv = this.cardCvv,
        cardAmount = this.cardAmount,
        cardLimit = this.cardLimit
    )
}
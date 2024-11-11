package com.nacho.restaurantapplication.presentation.viewmodel.neworder

import androidx.lifecycle.ViewModel
import com.nacho.restaurantapplication.core.fragment.state.PaymentMethodViewState
import com.nacho.restaurantapplication.core.utils.Constants
import com.nacho.restaurantapplication.domain.model.UserCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PaymentMethodViewModel @Inject constructor() : ViewModel() {

    private val _viewStatePaymentMethod = MutableStateFlow(PaymentMethodViewState())
    val viewStatePaymentMethod: StateFlow<PaymentMethodViewState> get() = _viewStatePaymentMethod

    fun onFieldsChangedPaymentMethod(userCard: UserCard, isCardValid: Boolean) {
        _viewStatePaymentMethod.value = userCard.toPaymentMethodViewState(isCardValid)
    }

    private fun isValidOrEmptyCardName(cardName: String): Boolean = cardName.length >= Constants.CARD_NAME_LENGTH || cardName.isEmpty()

    private fun isValidOrEmptyCardExpiration(cardDate: String): Boolean = cardDate.length >= Constants.EXPIRATION_CARD_LENGTH || cardDate.isEmpty()

    private fun isValidOrEmptyCardCvv(cardCvv: String): Boolean = cardCvv.length >= Constants.CVV_LENGTH || cardCvv.isEmpty()

    private fun UserCard.toPaymentMethodViewState(isCardValid: Boolean): PaymentMethodViewState {
        return PaymentMethodViewState(
            isValidCardNumber = isCardValid,
            isValidCardSince = isValidOrEmptyCardExpiration(cardSince),
            isValidCardUntil = isValidOrEmptyCardExpiration(cardUntil),
            isValidCardName = isValidOrEmptyCardName(cardName),
            isValidCardCvv = isValidOrEmptyCardCvv(cardCvv)
        )
    }

}